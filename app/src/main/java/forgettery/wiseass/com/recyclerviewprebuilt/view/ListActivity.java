/*
 * *
 *  * Copyright (C) 2017 Ryan Kay Open Source Project
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package forgettery.wiseass.com.recyclerviewprebuilt.view;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.transition.Fade;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
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
public class ListActivity extends AppCompatActivity implements ViewInterface, View.OnClickListener {

    private static final String EXTRA_DATE_AND_TIME = "EXTRA_DATE_AND_TIME";
    private static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    private static final String EXTRA_DRAWABLE = "EXTRA_DRAWABLE";

    /**
     * 2.
     * Obviously you wouldn't use such an ambiguous name in a non-demo App.
     */
    private List<ListItem> listOfData;

    //12. In order to create each ViewHolder in the UI, we need a LayoutInflater.
    private LayoutInflater layoutInflater;
    private RecyclerView recyclerView;
    private CustomAdapter adapter;
    private Toolbar toolbar;

    private Controller controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        recyclerView = (RecyclerView) findViewById(R.id.rec_list_activity);
        layoutInflater = getLayoutInflater();
        toolbar = (Toolbar) findViewById(R.id.tlb_list_activity);

        toolbar.setTitle(R.string.title_toolbar);
        toolbar.setLogo(R.drawable.ic_view_list_white_24dp);
        toolbar.setTitleMarginStart(72);

        FloatingActionButton fabulous = (FloatingActionButton) findViewById(R.id.fab_create_new_item);

        fabulous.setOnClickListener(this);

        controller = new Controller(this, new FakeDataSource());
    }

    /**
     * 17.
     * So, I'd normally just pass an Item's Unique ID (Key) to the other Activity, and then fetch
     * the Item from the Database their. However, this is a RecyclerView Demo App and I'm going to
     * simplify things like this. Also, by decomposing ListItem, it saves me having to make ListItem
     * Parcelable and bla bla bla whatever.
     *
     * @param dateAndTime
     * @param message
     * @param colorResource
     */
    @Override
    public void startDetailActivity(String dateAndTime, String message, int colorResource, View viewRoot) {
        Intent i = new Intent(this, DetailActivity.class);
        i.putExtra(EXTRA_DATE_AND_TIME, dateAndTime);
        i.putExtra(EXTRA_MESSAGE, message);
        i.putExtra(EXTRA_DRAWABLE, colorResource);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new Fade(Fade.IN));
            getWindow().setEnterTransition(new Fade(Fade.OUT));

            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation(this,
                            new Pair<View, String>(viewRoot.findViewById(R.id.imv_list_item_circle),
                                    getString(R.string.transition_drawable)),
                            new Pair<View, String>(viewRoot.findViewById(R.id.lbl_message),
                                    getString(R.string.transition_message)),
                            new Pair<View, String>(viewRoot.findViewById(R.id.lbl_date_and_time),
                                    getString(R.string.transition_time_and_date)));

            startActivity(i, options.toBundle());


        } else {
            startActivity(i);
        }
    }


    /**
     * In order to make sure things execute in the proper order, we have our Controller tell the
     * View when to set up it's stuff.
     *
     * @param listOfData
     */
    @Override
    public void setUpAdapterAndView(List<ListItem> listOfData) {
        this.listOfData = listOfData;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);

        adapter = new CustomAdapter();
        recyclerView.setAdapter(adapter);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(
                recyclerView.getContext(),
                layoutManager.getOrientation()
        );

        itemDecoration.setDrawable(
                ContextCompat.getDrawable(
                        ListActivity.this,
                        R.drawable.divider_white
                )
        );

        recyclerView.addItemDecoration(
                itemDecoration
        );

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(createHelperCallback());
        itemTouchHelper.attachToRecyclerView(recyclerView);


    }

    @Override
    public void addNewListItemToView(ListItem newItem) {
        listOfData.add(newItem);

        int endOfList = listOfData.size() - 1;

        adapter.notifyItemInserted(endOfList);

        recyclerView.smoothScrollToPosition(endOfList);
    }

    @Override
    public void deleteListItemAt(int position) {
        listOfData.remove(position);

        adapter.notifyItemRemoved(position);
    }

    @Override
    public void showUndoSnackbar() {
        Snackbar.make(
                findViewById(R.id.root_list_activity),
                getString(R.string.action_delete_item),
                Snackbar.LENGTH_LONG
        )
                .setAction(R.string.action_undo, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        controller.onUndoConfirmed();
                    }
                })
                .addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        super.onDismissed(transientBottomBar, event);

                        controller.onSnackbarTimeout();
                    }
                })
                .show();
    }

    @Override
    public void insertListItemAt(int position, ListItem listItem) {
        listOfData.add(position, listItem);

        adapter.notifyItemInserted(position);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.fab_create_new_item) {
            //User wishes to creat a new RecyclerView Item
            controller.createNewListItem();
        }
    }

    /**
     * 4.
     * (Opinion)
     * Why is the Adapter inside the Activity (a.k.a. a Nested Class)?
     * <p>
     * I used to pull this Class outside of the Activity and have it communicate back to the
     * Activity via Interface. This was actually detrimental in retrospect, as I ended up having to
     * manage a List of Data in the Activity, as well as a copy List of Data in the Adapter itself.
     * <p>
     * Also, since I needed to have my ViewHolder implement an OnClickListener, and then have the
     * result of that talk to the Activity through another interface, it was pretty confusing to
     * Beginners (not to mention pointless).
     * <p>
     * That being said, you don't *have* to nest the Adapter. Do what makes sense for the Software
     * Architecture in front of you.
     */
    private class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {//6

        /**
         * 13.
         * Inflates a new View (in this case, R.layout.item_data), and then creates/returns a new
         * CustomViewHolder object.
         *
         * @param parent   Unfortunately the docs currently don't explain this at all :(
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
         *
         * @param holder   The current ViewHolder instance for a given position
         * @param position The current position of the ViewHolder we are Binding to, based upon
         *                 our (listOfData). So for the second ViewHolder we create, we'll bind data
         *                 from the second Item in listOfData.
         */
        @Override
        public void onBindViewHolder(CustomAdapter.CustomViewHolder holder, int position) {
            //11. and now the ViewHolder data
            ListItem currentItem = listOfData.get(position);

            holder.coloredCircle.setImageResource(
                    currentItem.getColorResource()
            );

            holder.message.setText(
                    currentItem.getMessage()
            );

            holder.dateAndTime.setText(
                    currentItem.getDateAndTime()
            );

            holder.loading.setVisibility(View.INVISIBLE);
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

        /**
         * 5.
         * Each ViewHolder contains Bindings to the Views we wish to populate with Data.
         */
        class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            //10. now that we've made our layouts, let's bind them
            private CircleImageView coloredCircle;
            private TextView dateAndTime;
            private TextView message;
            private ViewGroup container;
            private ProgressBar loading;

            public CustomViewHolder(View itemView) {
                super(itemView);
                this.coloredCircle = (CircleImageView) itemView.findViewById(R.id.imv_list_item_circle);
                this.dateAndTime = (TextView) itemView.findViewById(R.id.lbl_date_and_time);
                this.message = (TextView) itemView.findViewById(R.id.lbl_message);
                this.loading = (ProgressBar) itemView.findViewById(R.id.pro_item_data);

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

            /**
             * 6.
             * Since I'm ok with the whole Container being the Listener, View v isn't super useful
             * in this Use Case. However, if I had a Single RecyclerView Item with multiple
             * Clickable Views, I could use v.getId() to tell which specific View was clicked.
             * See the comment within the method.
             *
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
                        listItem,
                        v
                );

            }
        }
    }

    private ItemTouchHelper.Callback createHelperCallback() {
        /*First Param is for Up/Down motion, second is for Left/Right.
        Note that we can supply 0, one constant (e.g. ItemTouchHelper.LEFT), or two constants (e.g.
        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) to specify what directions are allowed.
        */
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            //not used, as the first parameter above is 0
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                return false;
            }


            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int position = viewHolder.getAdapterPosition();
                controller.onListItemSwiped(
                        position,
                        listOfData.get(position)
                );
            }
        };

        return simpleItemTouchCallback;
    }
}
