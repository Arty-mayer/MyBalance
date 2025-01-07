package com.example.mybalance.data;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.mybalance.modelsDB.Accounts;
import com.example.mybalance.modelsDB.Currency;
import com.example.mybalance.modelsDB.ExpenseType;
import com.example.mybalance.modelsDB.ExpenseTypeDao;
import com.example.mybalance.modelsDB.Expenses;
import com.example.mybalance.modelsDB.ExpensesDao;
import com.example.mybalance.utils.DbTableSQL;

import java.util.concurrent.Executors;

import com.example.mybalance.modelsDB.AccountsDao;
import com.example.mybalance.modelsDB.CurrencyDao;
import com.example.mybalance.modelsDB.Income;
import com.example.mybalance.modelsDB.IncomeDao;
import com.example.mybalance.modelsDB.IncomeType;
import com.example.mybalance.modelsDB.IncomeTypeDao;
import com.example.mybalance.modelsDB.Month;
import com.example.mybalance.modelsDB.MonthDao;

@Database(entities = {Accounts.class, Expenses.class, Income.class, Month.class, Currency.class, IncomeType.class, ExpenseType.class}, version = 1)
public abstract class AppDB extends RoomDatabase {
    public static Context appContext;
    private static AppDB instance;

    public abstract AccountsDao accountsDao();

    public abstract ExpensesDao expensesDao();

    public abstract IncomeDao incomeDao();

    public abstract MonthDao monthDao();

    public abstract CurrencyDao currencyDao();

    public abstract IncomeTypeDao incomeTypeDao();

    public abstract ExpenseTypeDao expenseTypeDao();

    public static void setContext(Context context) {
        appContext = context.getApplicationContext();
    }

    public static AppDB getDb(Context context) {
       // if (instance == null) {
            synchronized (AppDB.class) {
                if (instance == null) {
                    setContext(context);
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDB.class, "MyBalanceDb")
                        //    .addMigrations(MIGRATION_1_2)
                            .addCallback(new Callback())
                            .build();
                }
            }
      //  }
        return instance;
    }

    private static class Callback extends RoomDatabase.Callback {

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            Executors.newSingleThreadExecutor().execute(() -> {

                for (String s : DbTableSQL.table_currency_date) {
                    db.execSQL(s);
                }

                for (String s : DbTableSQL.nullDates) {
                    db.execSQL(s);
                }
            });
        }
    }
/*
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
    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase supportSQLiteDatabase) {
            for (String s : DbTableSQL.migration_2_3_DBUpdate) {
                supportSQLiteDatabase.execSQL(s);
            }
        }
    };

    static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase supportSQLiteDatabase) {
            for (String s : DbTableSQL.migration_3_4_DBUpdate) {
                supportSQLiteDatabase.execSQL(s);
            }
            Executors.newSingleThreadExecutor().execute(() -> {
                AccountsDao accountsDao = instance.accountsDao();
                CurrencyDao currencyDao = instance.currencyDao();
                List<Accounts> accountslist = accountsDao.getAllAccounts();
                for (Accounts account : accountslist) {
                    Currency currency = currencyDao.getById(account.getCurrencyId());
                    account.setCurrencyCharCode(currency.getChar_code());
                    account.setCurrencySymbol(currency.getSymbol());
                    accountsDao.update(account);
                }
            });
        }
    };

 */
}
