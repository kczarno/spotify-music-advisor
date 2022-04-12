package advisor;

public class Main {
    public static void main(String[] args) {

        if (args.length > 1 && args[0].equals("-access")) {
            Authorisation.SERVER_PATH = args[1];
        }
        if (args.length > 1 && args[2].equals("-resource")) {
            Service.API_PATH = args[3] + "/v1/browse/";
        }
        if (args.length > 1 && args[4].equals("-page")) {
            Viewer.PAGES = Integer.parseInt(args[5]);
        }

        Advisor advisor = new Advisor();
        advisor.start();
    }
}