package advisor;

import java.util.Scanner;


public class Advisor {

    public void start() {

        Service service = new Service();
        Scanner scanner = new Scanner(System.in);
        String[] query = scanner.nextLine().split(" ");
        Viewer viewer;
        while (!query[0].equals("exit")) {
            switch (query[0]) {
                case ("auth"):
                    service.setAuthorization();
                    break;
                case ("new"):
                    viewer = new Viewer(service.getReleases());
                    viewer.viewItems();
                    break;
                case ("featured"):
                    viewer = new Viewer(service.getFeatured());
                    viewer.viewItems();
                    break;
                case ("categories"):
                    viewer = new Viewer(service.printCategories());
                    viewer.viewItems();
                    break;
                case ("playlists"):
                    StringBuilder sb = new StringBuilder();
                    for (int i = 1; i < query.length; i++) {
                        sb.append(query[i]).append(" ");
                    }
                    String playlistName = sb.substring(0, sb.length() - 1);
                    viewer = new Viewer(service.getPlaylists(playlistName));
                    viewer.viewItems();
                    sb.setLength(0);
                    break;
            }
            query = scanner.nextLine().split(" ");
        }
    }
}