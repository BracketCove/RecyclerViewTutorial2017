package forgettery.wiseass.com.recyclerviewprebuilt.data;

import java.util.List;

/**
 * This interface is the Contract which dictates how our Controller can talk to our Data Layer and
 * Vice Versa.
 *
 * @see <a href="https://www.youtube.com/edit?o=U&video_id=VCmi0gBxd0E">Android Java Interfaces by Example</a>
 * Created by R_KAY on 6/3/2017.
 */

public interface DataSourceInterface {

    List<ListItem> getListOfData();

}
