package Utils;

public class PageNavigator {

    private final PageHandler pageHandler;

    public  PageNavigator(PageHandler pageHandler) {
        this.pageHandler = pageHandler;
    }

    public void goToPage(int targetPage) {
        if (pageHandler.getCurrentPage() == targetPage) {
            return; // უკვე სწორ გვერდზე ვართ
        }

        pageHandler.resetToFirstPage(); // ვიწყებთ პირველიდან
        for (int i = 1; i < targetPage; i++) {
            pageHandler.next();
        }
    }
}
