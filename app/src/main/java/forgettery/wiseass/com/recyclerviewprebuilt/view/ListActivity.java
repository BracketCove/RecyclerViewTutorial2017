package forgettery.wiseass.com.recyclerviewprebuilt.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import forgettery.wiseass.com.recyclerviewprebuilt.R;
import forgettery.wiseass.com.recyclerviewprebuilt.data.FakeDataSource;
import forgettery.wiseass.com.recyclerviewprebuilt.data.ListItem;
import forgettery.wiseass.com.recyclerviewprebuilt.logic.Controller;

/**
 * 1.
 * List Activity is responsible for
 * - Coordinating the User Interface
 * - Relaying Click events to the Controller
 * - Starting a Detail Activity
 * -
 */
public class ListActivity extends AppCompatActivity implements ViewInterface {

    private static final String EXTRA_DATE_AND_TIME = "EXTRA_DATE_AND_TIME";
    private static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    private static final String EXTRA_COLOR = "EXTRA_COLOR";

    /**
     * 2.
     * Obviously you wouldn't use such an ambiguous name in a non-demo App.
     */
    private List<ListItem> listOfData;

    //12. In order to create each ViewHolder in the UI, we need a LayoutInflater.
    private LayoutInflater layoutInflater;
    private RecyclerView recyclerView;
    private CustomAdapter adapter;

    private Controller controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        recyclerView = (RecyclerView) findViewById(R.id.rec_list_activity);
        layoutInflater = getLayoutInflater();

        controller = new Controller(this, new FakeDataSource());
    }

    /** 17.
     * So, I'd normally just pass an Item's Unique ID (Key) to the other Activity, and then fetch
     * the Item from the Database their. However, this is a RecyclerView Demo App and I'm going to
     * simplify things like this. Also, by decomposing ListItem, it saves me having to make ListItem
     * Parcelable and bla bla bla whatever.
     *
     *
     * @param dateAndTime
     * @param message
     * @param colorResource
     */
    @Override
    public void startDetailActivity(String dateAndTime, String message, int colorResource) {
        Intent i = new Intent(this, DetailActivity.class);
        i.putExtra(EXTRA_DATE_AND_TIME, dateAndTime);
        i.putExtra(EXTRA_MESSAGE, message);
        i.putExtra(EXTRA_COLOR, colorResource);

        startActivity(i);
    }

    /**
     * In order to make sure things execute in the proper order, we have our Controller tell the
     * View when to set up it's stuff.
     * @param listOfData
     */
    @Override
    public void setUpAdapterAndView(List<ListItem> listOfData) {
        this.listOfData = listOfData;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CustomAdapter();
        recyclerView.setAdapter(adapter);
    }

    /**
     * 4.
     * (Opinion)
     * Why is the Adapter inside the Activity (a.k.a. a Nested Class)?
     *
     * I used to pull this Class outside of the Activity and have it communicate back to the
     * Activity via Interface. This was actually detrimental in retrospect, as I ended up having to
     * manage a List of Data in the Activity, as well as a copy List of Data in the Adapter itself.
     *
     * Also, since I needed to have my ViewHolder implement an OnClickListener, and then have the
     * result of that talk to the Activity through another interface, it was pretty confusing to
     * Beginners (not to mention pointless).
     *
     * That being said, you don't *have* to nest the Adapter. Do what makes sense for the Software
     * Architecture in front of you.
     *
     */
    private class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {//6

        /** 13.
         * Inflates a new View (in this case, R.layout.item_data), and then creates/returns a new
         * CustomViewHolder object.
         * @param parent Unfortunately the docs currently don't explain this at all :(
         * @param viewType Unfortunately the docs currently don't explain this at all :(
         * @return
         */
        @Override
        public CustomAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = layoutInflater.inflate(R.layout.item_data, parent, false);
            return new CustomViewHolder(v);
        }

        /**
         * This method "Binds" or assigns Data (from listOfData) to each View (ViewHolder).
         * @param holder The current ViewHolder instance for a given position
         * @param position The current position of the ViewHolder we are Binding to, based upon
         *                 our (listOfData). So for the second ViewHolder we create, we'll bind data
         *                 from the second Item in listOfData.
         */
        @Override
        public void onBindViewHolder(CustomAdapter.CustomViewHolder holder, int position) {
            //11. and now the ViewHolder data
            ListItem currentItem = listOfData.get(position);

            holder.coloredCircle.setBackgroundResource(
                   currentItem.getColorResource()
            );

            holder.message.setText(
                    currentItem.getMessage()
            );

            holder.dateAndTime.setText(
                    currentItem.getDateAndTime()
            );
        }

        /**
         * This method let's our Adapter determine how many ViewHolders it needs to create, based on
         * the size of the Dataset (List) which it is working with.
         *
         * @return the size of the dataset, generally via List.size()
         */
        @Override
        public int getItemCount() {
            // 12. Returning 0 here will tell our Adapter not to make any Items. Let's fix that.
            return listOfData.size();
        }

        /**5.
         * Each ViewHolder contains Bindings to the Views we wish to populate with Data.
         */
        class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            //10. now that we've made our layouts, let's bind them
            private View coloredCircle;
            private TextView dateAndTime;
            private TextView message;
            private ViewGroup container;

            public CustomViewHolder(View itemView) {
                super(itemView);
                this.coloredCircle = itemView.findViewById(R.id.imv_list_item_circle);
                this.dateAndTime = (TextView) itemView.findViewById(R.id.lbl_date_and_time);
                this.message = (TextView) itemView.findViewById(R.id.lbl_message);

                this.container = (ViewGroup) itemView.findViewById(R.id.root_list_item);
                /*
                We can pass "this" as an Argument, because "this", which refers to the Current
                Instance of type CustomViewHolder currently conforms to (implements) the
                View.OnClickListener interface. I have a Video on my channel which goes into
                Interfaces with Detailed Examples.

                Search "Android WTF: Java Interfaces by Example"
                 */
                this.container.setOnClickListener(this);
            }

            /** 6.
             * Since I'm ok with the whole Container being the Listener, View v isn't super useful
             * in this Use Case. However, if I had a Single RecyclerView Item with multiple
             * Clickable Views, I could use v.getId() to tell which specific View was clicked.
             * See the comment within the method.
             * @param v
             */
            @Override
            public void onClick(View v) {
                //getAdapterPosition() get's an Integer based on which the position of the current
                //ViewHolder (this) in the Adapter. This is how we get the correct Data.
                ListItem listItem = listOfData.get(
                        this.getAdapterPosition()
                );

                controller.onListItemClick(
                    listItem
                );

            }
        }
    }
}
