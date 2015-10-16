package comhk.musiccentric.callbacks;

import android.view.View;

import comhk.musiccentric.models.User;

/**
 * Created by charlton on 10/16/15.
 */

public interface OnIntroBackListener {
    public void OnButtonClickedFeedBack(View view, User user, boolean visible);
}
