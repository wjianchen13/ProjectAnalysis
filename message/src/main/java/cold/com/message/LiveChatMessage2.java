package cold.com.message;

import android.os.Message;
import android.view.View;

/**
 * Created by Administrator on 2017/6/15 0015.
 * 聊天信息，比赛活动
 */
public class LiveChatMessage2 extends BaseFeature  {

    private static final String TAG = LiveChatMessage2.class.getSimpleName();



    /************************************************************************************************
     *
     * construct
     *
     ***********************************************************************************************/
    public LiveChatMessage2() {
        super();
    }

    public void sendMineMessage(){
        sendEmptyMessage(1);
    }



    public void onClick(View v) {
        switch (v.getId()) {
            default:

                break;
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        System.out.println("-----------------------------> msg what: " + msg.what);
        return true;
    }



    /************************************************************************************************
     *
     * overrides
     *
     ***********************************************************************************************/


    @Override
    public void clear() {

    }

    /************************************************************************************************
     *
     * interface
     *
     ***********************************************************************************************/

    /************************************************************************************************
     *
     * getter & setter
     *
     ***********************************************************************************************/
}
