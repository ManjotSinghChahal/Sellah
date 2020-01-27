package com.app.admin.sellah.controller.Database;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {UserSearch.class},exportSchema = false,version = 1)
public abstract class MyDatabase extends RoomDatabase
{
  private static final String DB_NAME ="user_db";
  private static MyDatabase instance;

  public static synchronized MyDatabase getInstance(Context context)
  {
      if (instance==null)
      {
          instance = Room.databaseBuilder(context,MyDatabase.class,DB_NAME).allowMainThreadQueries().build();
      }

    return instance;
  }


    public abstract MyDao  myDao();
}
