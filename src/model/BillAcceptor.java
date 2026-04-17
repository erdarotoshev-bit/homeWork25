package model;

import java.util.Scanner;

public class BillAcceptor {
    private CoinAcceptor coinAcceptor;
    private Scanner scanner;

    public BillAcceptor (CoinAcceptor coinAcceptor, Scanner scanner) {
        this.coinAcceptor = coinAcceptor;
        this.scanner = new Scanner(System.in);
    }
    public int acceptBill() {
        while (true) {
            System.out.println("Введите сумму пополнения (или '0' для отмены):");
            String input = scanner.nextLine().trim();

            if ("0".equals(input) || "exit".equalsIgnoreCase(input)) {
                System.out.println("Пополнение отменено.");
                return 0;
            }

            try {
                int amount = Integer.parseInt(input);
                if (amount > 0) {
                    coinAcceptor.setAmount(coinAcceptor.getAmount() + amount);
                    return amount;
                } else {
                    System.out.println("Введите положительную сумму.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Введите целое число или '0' для выхода.");
            }
        }
    }
}
