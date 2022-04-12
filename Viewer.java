package advisor;

import java.util.List;
import java.util.Scanner;

public class Viewer {
    private List<String> items;
    public static int PAGES = 5;

    public Viewer(List<String> items) {
        this.items = items;
    }

    public void viewItems() {
        Scanner scanner = new Scanner(System.in);
        int page = 0;
        int totalPages = items.size() / PAGES;
        if (items.size() % PAGES > 0) {
            totalPages += 1;
        }
        paginator(page, totalPages);
        while (true) {
            String choice = scanner.nextLine();
            switch (choice) {
                case "prev":
                    if (page - 1 >= 0) {
                        page -= 1;
                        paginator(page, totalPages);
                    } else {
                        System.out.println("No more pages.");
                    }
                    break;
                case "next":
                    if (totalPages > page + 1) {
                        page += 1;
                        paginator(page, totalPages);
                    } else {
                        System.out.println("No more pages.");
                    }
                    break;
                case "exit":
                    return;
            }

        }
    }

    public void paginator(int page, int totalPages) {
        for (int i = page; i < PAGES + page; i++) {
            if (i <= items.size() - 1) {
                System.out.println(items.get(i));
            }
        }
        System.out.println("---PAGE " + (page + 1) + " OF " + totalPages +"---");
    }
}
