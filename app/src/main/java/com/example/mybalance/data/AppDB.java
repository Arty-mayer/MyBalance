package com.example.mybalance.data;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.mybalance.modelsDB.Accounts;
import com.example.mybalance.modelsDB.Currency;
import com.example.mybalance.modelsDB.Expenses;
import com.example.mybalance.modelsDB.ExpensesDao;
import com.example.mybalance.R;
import com.example.mybalance.Utils.DbTableSQL;

import java.util.concurrent.Executors;

import com.example.mybalance.modelsDB.AccountsDao;
import com.example.mybalance.modelsDB.CurrencyDao;
import com.example.mybalance.modelsDB.Income;
import com.example.mybalance.modelsDB.IncomeDao;
import com.example.mybalance.modelsDB.Month;
import com.example.mybalance.modelsDB.MonthDao;

@Database(entities = {Accounts.class, Expenses.class, Income.class, Month.class, Currency.class}, version = 3)
public abstract class AppDB extends RoomDatabase {
    private static Context appContext;
    private static AppDB instance;

    public abstract AccountsDao accountsDao();

    public abstract ExpensesDao expensesDao();

    public abstract IncomeDao incomeDao();

    public abstract MonthDao monthDao();

    public abstract CurrencyDao currencyDao();

    public static void setContext(Context context) {

        appContext = context.getApplicationContext();
    }

    // public static AppDB getDb(Context context){
    //     return AppDB.getDb(context, null);
    // }
    public static AppDB getDb(Context context) {
        if (instance == null) {
            synchronized (AppDB.class) {
                if (instance == null) {
                    setContext(context);
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDB.class, "MyBalanceDb")
                            .addMigrations(MIGRATION_1_2)
                            .addMigrations(MIGRATION_2_3)
                            .addCallback(new Callback())
                            .build();
                }
            }
        }
        return instance;
    }

    private static class Callback extends RoomDatabase.Callback {


        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            Executors.newSingleThreadExecutor().execute(() -> {

                AppDB database = instance;
                AccountsDao accountsDao = database.accountsDao();


                Accounts account = new Accounts();
                account.setName(appContext.getString(R.string.cash));
                account.setDescription("Иеющиейся наличные деньги");
                account.setAmount(0);
                account.setLastExpensesDate("");
                account.setLastIncomeDate("");
                account.setLastExpensesAmount(0);
                account.setLastIncomeAmount(0);
                account.setCurrencyId(118);
                accountsDao.insert(account);

                for (String s : DbTableSQL.table_currency_date) {
                    db.execSQL(s);
                }
            });
        }
    }

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase supportSQLiteDatabase) {
            for (String s : DbTableSQL.table_currency_create) {
                supportSQLiteDatabase.execSQL(s);
            }
            for (String s : DbTableSQL.table_currency_date) {
                supportSQLiteDatabase.execSQL(s);
            }
        }
    };
    static final Migration MIGRATION_2_3 = new Migration(2,3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase supportSQLiteDatabase) {
            for(String s : DbTableSQL.migration_2_3_DBUpdate){
                supportSQLiteDatabase.execSQL(s);
            }
        }
    };
}
