package com.app.admin.sellah.controller.Database;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface MyDao
{

    @Insert
    public void addSearch(UserSearch userSearch);

    @Query("Select * from USER_SEARCH")
    List<UserSearch> getSearchedList();

    @Delete
    public void deleteSearch(UserSearch userSearch);

}
