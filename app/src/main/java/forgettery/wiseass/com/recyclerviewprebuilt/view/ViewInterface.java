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

import android.view.View;

import java.util.List;

import forgettery.wiseass.com.recyclerviewprebuilt.data.ListItem;

/**
 * This Interface is the Contract which dictates how our View and Presenter can talk to each other.
 *
 * @see <a href="https://www.youtube.com/edit?o=U&video_id=VCmi0gBxd0E">Android Java Interfaces by Example</a>
 * Created by R_KAY on 6/3/2017.
 */

public interface ViewInterface {

    void startDetailActivity(String dateAndTime, String message, int colorResource, View viewRoot);

    void setUpAdapterAndView(List<ListItem> listOfData);

    void addNewListItemToView(ListItem newItem);

    void deleteListItemAt(int position);

    void showUndoSnackbar();

    void insertListItemAt(int temporaryListItemPosition, ListItem temporaryListItem);
}
