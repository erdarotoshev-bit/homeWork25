import enums.ActionLetter;
import model.*;
import util.UniversalArray;
import util.UniversalArrayImpl;

import java.util.Scanner;

public class AppRunner {

    private final UniversalArray<Product> products = new UniversalArrayImpl<>();
    private final CoinAcceptor coinAcceptor;
    private final BillAcceptor billAcceptor;
    private final Scanner scanner = new Scanner(System.in);

    private static boolean isExit = false;

    private AppRunner() {
        products.addAll(new Product[]{
                new Water(ActionLetter.B, 20),
                new CocaCola(ActionLetter.C, 50),
                new Soda(ActionLetter.D, 30),
                new Snickers(ActionLetter.E, 80),
                new Mars(ActionLetter.F, 80),
                new Pistachios(ActionLetter.G, 130)
        });
        coinAcceptor = new CoinAcceptor(100);
        billAcceptor = new BillAcceptor(coinAcceptor, scanner);
    }

    public static void run() {
        AppRunner app = new AppRunner();
        while (!isExit) {
            app.startSimulation();
        }
    }

    private void startSimulation() {
        print("В автомате доступны:");
        showProducts(products);

        print("Ваш текущий баланс: " + coinAcceptor.getAmount());

        UniversalArray<Product> allowProducts = getAllowedProducts();
        chooseAction(allowProducts);
    }

    private UniversalArray<Product> getAllowedProducts() {
        UniversalArray<Product> allowProducts = new UniversalArrayImpl<>();
        for (int i = 0; i < products.size(); i++) {
            if (coinAcceptor.getAmount() >= products.get(i).getPrice()) {
                allowProducts.add(products.get(i));
            }
        }
        return allowProducts;
    }

    private void chooseAction(UniversalArray<Product> allowProducts) {
        print("------------------------------------------");
        print(" a - Пополнить баланс");
        showActions(allowProducts);
        print(" h - Выйти");

        String input = fromConsole().trim();
        if (input.isEmpty()) return;

        String action = input.substring(0, 1).toLowerCase();

        if ("a".equals(action)) {
            int amount = billAcceptor.acceptBill();
            if (amount > 0) {
                print("Вы успешно пополнили баланс на: " + amount);
            }
            return;
        }

        if ("h".equals(action)) {
            isExit = true;
            return;
        }

        try {
            ActionLetter selectedLetter = ActionLetter.valueOf(action.toUpperCase());
            boolean purchased = false;

            for (int i = 0; i < allowProducts.size(); i++) {
                Product p = allowProducts.get(i);
                if (p.getActionLetter().equals(selectedLetter)) {
                    coinAcceptor.setAmount(coinAcceptor.getAmount() - p.getPrice());
                    print("Вы купили " + p.getName());
                    purchased = true;
                    break;
                }
            }

            if (!purchased) {
                print("Товар недоступен. Проверьте баланс или букву.");
            }

        } catch (IllegalArgumentException e) {
            print("Недопустимая команда. Попробуйте еще раз.");
        }
    }

    private void showActions(UniversalArray<Product> products) {
        if (products.size() == 0) {
            print(" (Нет доступных товаров для вашего баланса)");
        }
        for (int i = 0; i < products.size(); i++) {
            print(String.format(" %s - %s",
                    products.get(i).getActionLetter().getValue(),
                    products.get(i).getName(),
                    products.get(i).getPrice()));
        }
    }

    private String fromConsole() {
        return scanner.nextLine();
    }

    private void showProducts(UniversalArray<Product> products) {
        for (int i = 0; i < products.size(); i++) {
            print(products.get(i).toString());
        }
    }

    private void print(String msg) {
        System.out.println(msg);
    }
}