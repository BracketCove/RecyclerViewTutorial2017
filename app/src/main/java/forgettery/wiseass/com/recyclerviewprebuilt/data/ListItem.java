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

package forgettery.wiseass.com.recyclerviewprebuilt.data;

/**
 * 3.
 * This class describes an "Item" of "Data". In a real App, I'd call them things like:
 * - Workout
 * - Reminder
 * - Task
 * - Note
 * - User
 * <p>
 * Created by R_KAY on 5/31/2017.
 */

public class ListItem {

    private String dateAndTime;
    private String message;
    private int colorResource;

    /*It's common for an "Item" to have a unique Id for storing an a Database
    private String uniqueIdentifier;*/

    public ListItem(String dateAndTime, String message, int colorResource) {
        this.dateAndTime = dateAndTime;
        this.message = message;
        this.colorResource = colorResource;
    }

    public int getColorResource() {
        return colorResource;
    }

    public void setColorResource(int colorResource) {
        this.colorResource = colorResource;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
