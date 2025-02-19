package models;

import android.os.Parcel;
import android.os.Parcelable;

public class OnboardingItem implements Parcelable {
    private static final int TOTAL_PAGES = 3; // 🔹 Dynamic for easy changes

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
        return position + "/" + TOTAL_PAGES; // 🔹 No hardcoded "3", uses TOTAL_PAGES
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

    // 🔹 Parcelable Implementation for easy object passing
    protected OnboardingItem(Parcel in) {
        position = in.readInt();
        imageRes = in.readInt();
        title = in.readString();
        subtitle = in.readString();
    }

    public static final Creator<OnboardingItem> CREATOR = new Creator<OnboardingItem>() {
        @Override
        public OnboardingItem createFromParcel(Parcel in) {
            return new OnboardingItem(in);
        }

        @Override
        public OnboardingItem[] newArray(int size) {
            return new OnboardingItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(position);
        dest.writeInt(imageRes);
        dest.writeString(title);
        dest.writeString(subtitle);
    }
}
