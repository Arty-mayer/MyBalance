package com.mybalance.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybalance.R;
import com.mybalance.utils.Constante;
import com.mybalance.data.AppDB;
import com.mybalance.modelsDB.Accounts;
import com.mybalance.modelsDB.AccountsDao;
import com.mybalance.modelsDB.Expenses;
import com.mybalance.modelsDB.ExpensesDao;
import com.mybalance.modelsDB.Income;
import com.mybalance.modelsDB.IncomeDao;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;

public class FragmentHome extends Fragment {
    public static final int MAIN_CURRENCY_THIS_MONTH = 1;
    public static final int MAIN_CURRENCY_MONTH = 2;
    public static final int OTHER_CURRENCY_MONTH = 3;

    TextView notice1;
    TextView notice2;
    TextView notice3;
    TextView thisMonthAmount;
    TextView lastMonthAmount;
    ImageView thisMonthImage;
    ImageView lastMonthImage;
    Spinner spinner1;
    Spinner spinner2;
    RecyclerView mainCurrencyRV;
    RecyclerView mainCurrencyMonthRV;
    RecyclerView otherCurrencyRV;
    TextView otherCurrencysNotice;
    ProgressBar progressBar1;
    ProgressBar progressBar2;
    ProgressBar progressBar3;

    ArrayAdapter<String> arrayAdapter1;
    ArrayAdapter<String> arrayAdapter2;


    AccountsAdapterForHomeRV adapterForRVHomeMainThisMonth;
    AccountsAdapterForHomeRV adapterForRVHomeMainMonth;
    AccountsAdapterForHomeRV adapterForRVHomeOtherMonth;

    Pair<Drawable, Drawable> drawableArrows;

    SharedPreferences appPreferences;

    Pair<LocalDate, LocalDate> datePairForMain;
    Pair<LocalDate, LocalDate> datePairForOther;

    //db
    AccountsDao accountsDao;
    IncomeDao incomeDao;
    ExpensesDao expensesDao;


    //dates

    LocalDate today;
    List<Accounts> mainCurrencyAccounts;
    List<Accounts> otherCurrencyAccounts;
    List<AccountForRView> listMainThisMonth;
    List<AccountForRView> listMainMonth;
    List<AccountForRView> listOtherMonth;

    List<YearMonth> listForSpinner;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findInterfaceItems(view);
        initialization();
        setListeners();
        loadDatesFromDb();
    }

    private void setListeners() {
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                YearMonth selectedYM = listForSpinner.get(position);
                datePairForMain = new Pair<>(LocalDate.of(selectedYM.getYear(), selectedYM.getMonth(), 1), selectedYM.atEndOfMonth());
                getMainCurrencyAccountsDatesFromDb();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                YearMonth selectedYM = listForSpinner.get(position);
                datePairForOther = new Pair<>(LocalDate.of(selectedYM.getYear(), selectedYM.getMonth(), 1), selectedYM.atEndOfMonth());
                getOtherCurrencyAccountsDatesFromDb();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getMainCurrencyAccountsDatesFromDb() {
        setInterfacePartVisibility(MAIN_CURRENCY_MONTH, View.INVISIBLE);
        progressBar2.setVisibility(View.VISIBLE);
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                listMainMonth = makeListForRV(datePairForMain.first, datePairForMain.second, mainCurrencyAccounts);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        makeInterfaceMainMonth();
                    }
                });
            }
        });
    }

    private void getOtherCurrencyAccountsDatesFromDb() {
        setInterfacePartVisibility(OTHER_CURRENCY_MONTH, View.INVISIBLE);
        // setInterfacePartVisibility(OTHER_CURRENCY_MONTH, View.VISIBLE);
        progressBar3.setVisibility(View.VISIBLE);
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                listOtherMonth = makeListForRV(datePairForOther.first, datePairForOther.second, otherCurrencyAccounts);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        makeInterfaceOther();
                    }
                });
            }
        });
    }

    private void initialization() {
        Context context = this.getActivity();
        drawableArrows = new Pair<>(context.getDrawable(R.drawable.arrow_up), context.getDrawable(R.drawable.arrow_dn));

        appPreferences = context.getSharedPreferences(Constante.preferences, context.MODE_PRIVATE);

        Calendar calendar = Calendar.getInstance();
        today = LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE));
        YearMonth yearMonth = YearMonth.of(today.getYear(), today.getMonth()).minusMonths(1);
        datePairForMain = new Pair<>(LocalDate.of(yearMonth.getYear(), yearMonth.getMonth(), 1), yearMonth.atEndOfMonth());
        datePairForOther = new Pair<>(LocalDate.of(yearMonth.getYear(), yearMonth.getMonth(), 1), yearMonth.atEndOfMonth());

        mainCurrencyRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mainCurrencyMonthRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        otherCurrencyRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        adapterForRVHomeMainThisMonth = new AccountsAdapterForHomeRV();
        adapterForRVHomeMainMonth = new AccountsAdapterForHomeRV();
        adapterForRVHomeOtherMonth = new AccountsAdapterForHomeRV();

        mainCurrencyRV.setAdapter(adapterForRVHomeMainThisMonth);
        mainCurrencyMonthRV.setAdapter(adapterForRVHomeMainMonth);
        otherCurrencyRV.setAdapter(adapterForRVHomeOtherMonth);

        List<String> itemslist = new ArrayList<String>();

        listForSpinner = new ArrayList<YearMonth>();
        yearMonth = YearMonth.of(today.getYear(), today.getMonth());
        for (int i = 0; i < 12; i++) {

            listForSpinner.add(yearMonth.minusMonths(i));
            String s = getMonthFromResources(listForSpinner.get(i).getMonth());
            s = s + " " + String.valueOf(listForSpinner.get(i).getYear());
            itemslist.add(s);
        }

        arrayAdapter1 = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        arrayAdapter1.addAll(itemslist);

        arrayAdapter2 = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        arrayAdapter2.addAll(itemslist);

        spinner1.setAdapter(arrayAdapter1);
        spinner1.setSelection(1);
        spinner2.setAdapter(arrayAdapter2);
    }

    private String getMonthFromResources(Month m) {
        String month = "";
        switch (m) {
            case JANUARY:
                month = getString(R.string.january);
                break;
            case FEBRUARY:
                month = getString(R.string.february);
                break;
            case MARCH:
                month = getString(R.string.march);
                break;
            case APRIL:
                month = getString(R.string.april);
                break;
            case MAY:
                month = getString(R.string.may);
                break;
            case JUNE:
                month = getString(R.string.june);
                break;
            case JULY:
                month = getString(R.string.july);
                break;
            case AUGUST:
                month = getString(R.string.august);
                break;
            case SEPTEMBER:
                month = getString(R.string.september);
                break;
            case OCTOBER:
                month = getString(R.string.october);
                break;
            case NOVEMBER:
                month = getString(R.string.november);
                break;
            case DECEMBER:
                month = getString(R.string.december);
                break;
        }
        return month;
    }

    private void findInterfaceItems(View view) {
        notice1 = view.findViewById(R.id.notice1);
        notice2 = view.findViewById(R.id.notice2);
        notice3 = view.findViewById(R.id.notice3);
        spinner1 = view.findViewById(R.id.homeSpinner1);
        spinner2 = view.findViewById(R.id.homeSpinner2);
        thisMonthAmount = view.findViewById(R.id.TVThisMonth);
        lastMonthAmount = view.findViewById(R.id.TVlasstMonth);
        thisMonthImage = view.findViewById(R.id.IVThisMonth);
        lastMonthImage = view.findViewById(R.id.IVLastMonth);
        mainCurrencyRV = view.findViewById(R.id.RVMainCurrencyAccounts);
        mainCurrencyMonthRV = view.findViewById(R.id.RVMainCurrencyAccountsMonth);
        otherCurrencyRV = view.findViewById(R.id.RVAnotherCurrencyAccounts);
        otherCurrencysNotice = view.findViewById(R.id.notice3);
        progressBar1 = view.findViewById(R.id.progressBar1);
        progressBar2 = view.findViewById(R.id.progressBar2);
        progressBar3 = view.findViewById(R.id.progressBar3);

    }

    private void setInterfacePartVisibility(int part, int visibility) {
        switch (part) {
            case FragmentHome.MAIN_CURRENCY_THIS_MONTH:
                notice2.setVisibility(visibility);
                thisMonthImage.setVisibility(visibility);
                thisMonthAmount.setVisibility(visibility);
                mainCurrencyRV.setVisibility(visibility);
                break;
            case FragmentHome.MAIN_CURRENCY_MONTH:
                spinner1.setVisibility(visibility);
                lastMonthImage.setVisibility(visibility);
                lastMonthAmount.setVisibility(visibility);
                mainCurrencyMonthRV.setVisibility(visibility);
                break;
            case FragmentHome.OTHER_CURRENCY_MONTH:
                notice3.setVisibility(visibility);
                spinner2.setVisibility(visibility);
                otherCurrencyRV.setVisibility(visibility);
                break;
        }
    }

    private void loadDatesFromDb() {
        setInterfacePartVisibility(MAIN_CURRENCY_THIS_MONTH, View.INVISIBLE);
        progressBar1.setVisibility(View.VISIBLE);
        final int mainCurrencyId = appPreferences.getInt(Constante.defCurrencyId, 118);
        if (mainCurrencyId == 0) {
            return;
        }
        YearMonth yearMonth = YearMonth.of(today.getYear(), today.getMonth());
        LocalDate date1 = LocalDate.of(today.getYear(), today.getMonth(), 1);
        LocalDate date2 = yearMonth.atEndOfMonth();

        AppDB db = AppDB.getDb(this.getActivity());
        if (accountsDao == null) {
            accountsDao = db.accountsDao();
            expensesDao = db.expensesDao();
            incomeDao = db.incomeDao();
        }
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                mainCurrencyAccounts = accountsDao.getAccountsByCurrencyId(mainCurrencyId);
                otherCurrencyAccounts = accountsDao.getAccountsByCurrencyIdExclude(mainCurrencyId);

                listMainThisMonth = makeListForRV(date1, date2, mainCurrencyAccounts);
                listMainMonth = makeListForRV(datePairForMain.first, datePairForMain.second, mainCurrencyAccounts);

                listOtherMonth = makeListForRV(datePairForOther.first, datePairForOther.second, otherCurrencyAccounts);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        makeInterfaceMainThisMonth();
                        //   makeInterfaceMainMonth();
                        //    makeInterfaceOther();
                    }
                });
            }
        });
    }

    /**
     * @param date1 начальная дата диапазона
     * @param date2 конечная дата диапазона
     * @param list  список аккаунтов
     * @return список данных для использования в RecyclerView
     * @brief вызывать только НЕ в основном потоке
     */
    private List<AccountForRView> makeListForRV(LocalDate date1, LocalDate date2, List<Accounts> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        List<AccountForRView> listAccountsForRV = new ArrayList<>();
        for (Accounts a : list) {
            listAccountsForRV.add(makeAccountsForRV(a, date1, date2));
        }
        return listAccountsForRV;
    }

    /**
     * @param account аккаунт для обработаки
     * @param date1   начальная дата диапазона
     * @param date2   конечная дата диапазона
     * @return список данных для использования в RecyclerView
     * @brief вызывать только НЕ в основном потоке
     */
    private AccountForRView makeAccountsForRV(Accounts account, LocalDate date1, LocalDate date2) {

        float incomes = 0;
        float expenses = 0;
        List<Income> listIncome = incomeDao.getIncomesRangeByAccount(date1.toString(), date2.toString(), account.getId());
        List<Expenses> listExpenses = expensesDao.getExpensesRangeByAccount(date1.toString(), date2.toString(), account.getId());
        if (!listIncome.isEmpty()) {
            for (Income in : listIncome) {
                incomes += in.getAmount();
            }
        }
        if (!listExpenses.isEmpty()) {
            for (Expenses exp : listExpenses) {
                expenses += exp.getAmount();
            }
        }
        AccountForRView accountForRView = new AccountForRView();
        accountForRView.accountsName = account.getName();
        accountForRView.Amount = account.getAmount();
        accountForRView.currency = account.getCurrencyCharCode();
        accountForRView.symbol = account.getCurrencySymbol();
        accountForRView.incomeAmount = incomes;
        accountForRView.expenseAmount = expenses;
        accountForRView.difference = incomes - expenses;

        return accountForRView;
    }

    @SuppressLint("ResourceAsColor")
    private void makeInterfaceMainThisMonth() {
        if (listMainThisMonth == null || listMainThisMonth.isEmpty()) {
            progressBar1.setVisibility(View.GONE);
            return;
        }
        float difThisMonth = 0;
        for (AccountForRView acc : listMainThisMonth) {
            difThisMonth += acc.difference;
        }
        String text = "";
        if (listMainThisMonth.get(0).symbol.length() > 0) {
            text = text + listMainThisMonth.get(0).symbol + " ";
        } else {
            text = text + listMainThisMonth.get(0).currency + " ";
        }
        text = text + String.valueOf(difThisMonth);
        thisMonthAmount.setText(text);
        if (difThisMonth < 0) {
            thisMonthAmount.setTextColor(Color.RED);
            thisMonthImage.setImageDrawable(drawableArrows.second);
        } else {
            thisMonthAmount.setTextColor(Color.GREEN);
            thisMonthImage.setImageDrawable(drawableArrows.first);
        }

        adapterForRVHomeMainThisMonth.updateList(listMainThisMonth);
        progressBar1.setVisibility(View.GONE);
        setInterfacePartVisibility(MAIN_CURRENCY_THIS_MONTH, View.VISIBLE);
    }

    @SuppressLint("ResourceAsColor")
    private void makeInterfaceMainMonth() {
        if (listMainMonth == null || listMainMonth.isEmpty()) {
            progressBar2.setVisibility(View.GONE);
            return;
        }
        float difThisMonth = 0;
        for (AccountForRView acc : listMainMonth) {
            difThisMonth += acc.difference;
        }
        String text = "";
        if (listMainMonth.get(0).symbol.length() > 0) {
            text = text + listMainMonth.get(0).symbol + " ";
        } else {
            text = text + listMainMonth.get(0).currency + " ";
        }
        text = text + String.valueOf(difThisMonth);
        lastMonthAmount.setText(text);
        if (difThisMonth < 0) {
            lastMonthAmount.setTextColor(Color.RED);
            lastMonthImage.setImageDrawable(drawableArrows.second);
        } else {
            lastMonthAmount.setTextColor(Color.GREEN);
            lastMonthImage.setImageDrawable(drawableArrows.first);
        }
        progressBar2.setVisibility(View.GONE);
        setInterfacePartVisibility(MAIN_CURRENCY_MONTH, View.VISIBLE);
        adapterForRVHomeMainMonth.updateList(listMainMonth);
    }

    private void makeInterfaceOther() {
        progressBar3.setVisibility(View.GONE);
        if (listOtherMonth == null || listOtherMonth.isEmpty()) {
            setInterfacePartVisibility(OTHER_CURRENCY_MONTH, View.INVISIBLE);
            return;
        } else {
            setInterfacePartVisibility(OTHER_CURRENCY_MONTH, View.VISIBLE);
        }
        setInterfacePartVisibility(OTHER_CURRENCY_MONTH, View.VISIBLE);
        adapterForRVHomeOtherMonth.updateList(listOtherMonth);
    }

}
