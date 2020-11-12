package kheloindia.com.assessment.mvp;

public interface MainView<T> {

    void showError(String response);
    /**
     * Shows loading layout on the view.
     */
    void showLoadingLayout();
    /**
     * Hides loading layout.
     */
    void hideLoadingLayout();
}