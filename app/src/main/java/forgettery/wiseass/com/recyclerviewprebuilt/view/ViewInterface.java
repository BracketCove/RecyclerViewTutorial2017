package forgettery.wiseass.com.recyclerviewprebuilt.view;

import java.util.List;

import forgettery.wiseass.com.recyclerviewprebuilt.data.ListItem;

/**
 * This Interface is the Contract which dictates how our View and Presenter can talk to each other.
 *
 * @see <a href="https://www.youtube.com/edit?o=U&video_id=VCmi0gBxd0E">Android Java Interfaces by Example</a>
 * Created by R_KAY on 6/3/2017.
 */

public interface ViewInterface {

    void startDetailActivity(String dateAndTime, String message, int colorResource);

    void setUpAdapterAndView(List<ListItem> listOfData);

}
