package kheloindia.com.assessment.mvp;

public interface IBasePresenter<ViewT> {

    void onViewActive(ViewT view);

    void onViewInActive();
}