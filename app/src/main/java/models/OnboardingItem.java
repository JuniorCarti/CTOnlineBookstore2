package models;


public class OnboardingItem {
    private final int position;
    private final int imageRes;
    private final String title;
    private final String subtitle;

    public OnboardingItem(int position, int imageRes, String title, String subtitle) {
        this.position = position;
        this.imageRes = imageRes;
        this.title = title;
        this.subtitle = subtitle;
    }

    public String getCounterText() {
        return position + "/3";
    }

    public int getImageRes() {
        return imageRes;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }
}
