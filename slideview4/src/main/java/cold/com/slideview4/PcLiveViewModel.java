package cold.com.slideview4;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

/**
 * Created by Administrator on 2017/6/13 0013.
 */
public class PcLiveViewModel  extends BaseObservable {

    private String txt = "123456";

    public PcLiveViewModel() {

    }

    @Bindable
    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;

    }
}
