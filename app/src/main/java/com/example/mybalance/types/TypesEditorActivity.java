package com.example.mybalance.types;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Transaction;

import com.example.mybalance.R;
import com.example.mybalance.data.AppDB;
import com.example.mybalance.modelsDB.ExpenseType;
import com.example.mybalance.modelsDB.ExpenseTypeDao;
import com.example.mybalance.modelsDB.IncomeType;
import com.example.mybalance.modelsDB.IncomeTypeDao;
import com.example.mybalance.modelsDB.InterfaceForTypes;
import com.google.android.material.tabs.TabLayout;

import java.util.List;
import java.util.concurrent.Executors;

public class TypesEditorActivity extends AppCompatActivity {
    public static final int FOR_INCOME = 1;
    public static final int FOR_EXPENSE = 0;

    RecyclerView recyclerViewForTypes;
    Button addButton;
    Button closeButton;
    TabLayout tabLayout;
    EditText editTextForName;
    IncomeTypeDao incomeTypeDao;
    ExpenseTypeDao expenseTypeDao;

    //data
    ViewModelForTypes liveData;
    AdapterForRVTypes adapterForExp;
    AdapterForRVTypes adapterForInc;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.types_exp_in);
        initialization();
        setListeners();
        setObservers();
        loadTypesInFromDB();
        addItemTouchHelper();
    }

    private void initialization() {
        findInterfaceItems();
        liveData = new ViewModelForTypes();
        recyclerViewForTypes.setLayoutManager(new LinearLayoutManager(this));
        EditHandler editHandler = createEditHandler();
        adapterForExp = new AdapterForRVTypes(FOR_EXPENSE, editHandler);
        adapterForInc = new AdapterForRVTypes(FOR_INCOME, editHandler);
        if (editTextForName.getText().length() < 4) {
            addButton.setEnabled(false);
        }
        recyclerViewForTypes.setAdapter(adapterForExp);

    }

    private EditHandler createEditHandler() {
        return new EditHandler() {
            @Override
            public void onSave(int position, String newName, int forType) {
                setDaos();
                if (forType == TypesEditorActivity.FOR_EXPENSE) {
                    ExpenseType expType = (ExpenseType) adapterForExp.getType(position);
                    expType.setName(newName);
                    Executors.newSingleThreadExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            expenseTypeDao.update(expType);
                        }
                    });
                } else if (forType == TypesEditorActivity.FOR_INCOME) {
                    IncomeType incType = (IncomeType) adapterForInc.getType(position);
                    incType.setName(newName);
                    Executors.newSingleThreadExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            incomeTypeDao.update(incType);
                        }
                    });
                }
            }
        };
    }

    private void findInterfaceItems() {
        tabLayout = findViewById(R.id.tabLayout);
        addButton = findViewById(R.id.addButton);
        closeButton = findViewById(R.id.closeButton);
        editTextForName = findViewById(R.id.newTypeName);
        recyclerViewForTypes = findViewById(R.id.RVTypes);

    }

    private void sendEndsOfEdition() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        View focus = this.getCurrentFocus();
        if (focus != null) {
            IBinder a = focus.getWindowToken();
            imm.hideSoftInputFromWindow(a, 0);
        }
        LinearLayoutManager manager = (LinearLayoutManager) recyclerViewForTypes.getLayoutManager();
        for (int i = manager.findFirstVisibleItemPosition(); i <= manager.findLastVisibleItemPosition(); i++) {
            ViewHolderForTypes viewHolder = (ViewHolderForTypes) recyclerViewForTypes.findViewHolderForLayoutPosition(i);
            viewHolder.endsOfEdition();
        }
    }

    private void setListeners() {
        recyclerViewForTypes.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    sendEndsOfEdition();
                }
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    recyclerViewForTypes.setAdapter(adapterForExp);
                    recyclerViewForTypes.scheduleLayoutAnimation();
                } else if (tab.getPosition() == 1) {
                    recyclerViewForTypes.setAdapter(adapterForInc);
                    recyclerViewForTypes.scheduleLayoutAnimation();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        editTextForName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                addButton.setEnabled(s.length() > 4);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTypeInDB();
            }
        });
    }

    /**
     * @param type - объект для вставки
     * @return id  - id ставленной записи или 0 в случае неудачи.
     * Метод не может быть вызван в UIThread !!!
     */
    @Transaction
    private long addTransaction(ExpenseType type) {
        long endTime = System.currentTimeMillis() + 10000;
        boolean success = false;
        Long maxSortOrder = expenseTypeDao.getMaxSortOrder();
        long newSortOrder = (maxSortOrder == null) ? 0 : maxSortOrder + 1;
        type.setSortOrder(newSortOrder);
        long id = 0;

        while (!success) {
            try {
                if (System.currentTimeMillis() > endTime) {
                    break;
                }
                id = expenseTypeDao.insert(type);
                success = true;
            } catch (SQLiteConstraintException e) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        return id;
    }

    @Transaction
    private long addTransaction(IncomeType type) {
        long endTime = System.currentTimeMillis() + 10000;
        boolean success = false;
        Long maxSortOrder = incomeTypeDao.getMaxSortOrder();
        long newSortOrder = (maxSortOrder == null) ? 0 : maxSortOrder + 1;
        type.setSortOrder(newSortOrder);
        long id = 0;

        while (!success) {
            try {
                if (System.currentTimeMillis() > endTime) {
                    break;
                }
                id = incomeTypeDao.insert(type);
                success = true;
            } catch (SQLiteConstraintException e) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        return id;
    }

    private void addTypeInDB() {
        if (editTextForName.getText().length() < 5) {
            return;
        }
        setDaos();
        final String inputString = editTextForName.getText().toString();
        final int tabOption = tabLayout.getSelectedTabPosition();

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                switch (tabOption) {
                    case 0:
                        ExpenseType type = new ExpenseType(inputString);
                        final long id = addTransaction(type);
                        if (id > 0) {
                            type.setId(id);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapterForExp.addItem(type);
                                    recyclerViewForTypes.scrollToPosition(adapterForExp.getItemCount() - 1);
                                }
                            });
                        }

                        break;
                    case 1:
                        IncomeType type1 = new IncomeType(inputString);
                        final long id1 = addTransaction(type1);
                        if (id1 > 0) {
                            type1.setId(id1);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapterForInc.addItem(type1);
                                    recyclerViewForTypes.scrollToPosition(adapterForInc.getItemCount() - 1);
                                }
                            });
                        }
                        break;
                }
            }
        });
        editTextForName.setText("");
        editTextForName.clearFocus();
    }

    @Transaction
    private void deleteTransaction(ExpenseType type) {
        long deletedSortOrder = type.getSortOrder();
        expenseTypeDao.delete(type);
        expenseTypeDao.updateSortOrderForDelete(deletedSortOrder);
    }

    @Transaction
    private void deleteTransaction(IncomeType type) {
        long deletedSortOrder = type.getSortOrder();
        incomeTypeDao.delete(type);
        incomeTypeDao.updateSortOrderForDelete(deletedSortOrder);
    }

    private void deleteTypeHandler(int position) {
        AdapterForRVTypes adapter = (AdapterForRVTypes) recyclerViewForTypes.getAdapter();
        final InterfaceForTypes type = adapter.getType(position);
        if (type.getId() == 1) {
            Toast.makeText(this, "Эту категорию удалить нельзя", Toast.LENGTH_LONG).show();
            return;
        }
        new AlertDialog.
                Builder(this).
                setTitle(R.string.confirmation).
                setMessage(R.string.delete1).
                setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        adapter.deleteItem(position);
                        Executors.newSingleThreadExecutor().execute(new Runnable() {
                            @Override
                            public void run() {
                                if (type instanceof ExpenseType) {
                                    deleteTransaction((ExpenseType) type);
                                } else if (type instanceof IncomeType) {
                                    deleteTransaction((IncomeType) type);
                                }
                            }
                        });
                    }
                }).
                setNeutralButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        recyclerViewForTypes.getAdapter().notifyItemChanged(position);
                    }
                }).
                show();
    }

    private void setDaos() {
        if (incomeTypeDao == null) {
            AppDB db = AppDB.getDb(this);
            incomeTypeDao = db.incomeTypeDao();
            expenseTypeDao = db.expenseTypeDao();
        }
    }

    private void loadTypesInFromDB() {
        setDaos();
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                liveData.setListExp(expenseTypeDao.getAllExpenseTypes());
                liveData.setListInc(incomeTypeDao.getAllIncomeTypes());
            }
        });
    }

    private void setObservers() {
        liveData.getListExp().observe(this, new Observer<List<ExpenseType>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(List<ExpenseType> expenseTypes) {
                adapterForExp.setList(expenseTypes);
                adapterForExp.notifyDataSetChanged();

            }
        });

        liveData.getListInc().observe(this, new Observer<List<IncomeType>>() {
            @Override
            public void onChanged(List<IncomeType> incomeTypes) {
                adapterForInc.setList(incomeTypes);
                adapterForInc.notifyDataSetChanged();
            }
        });
    }

    private void moveSortOrderInDB(AdapterForRVTypes adapter) {
        final int[] movePositions = adapter.getMovePositions();
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            @Transaction
            public void run() {
                adapter.organizeSortOrdersInList();
                setDaos();
                switch (adapter.forType) {
                    case TypesEditorActivity.FOR_EXPENSE:
                        expenseTypeDao.setSortOrderTemp(movePositions[0]);
                        if (movePositions[0] > movePositions[1]) {
                            expenseTypeDao.updateSortOrderDown(movePositions[0], movePositions[1]);
                        } else if (movePositions[0] < movePositions[1]) {
                            expenseTypeDao.updateSortOrderUp(movePositions[0], movePositions[1]);
                        }
                        expenseTypeDao.setSortOrderNewPos(movePositions[1]);
                        break;

                    case TypesEditorActivity.FOR_INCOME:
                        incomeTypeDao.setSortOrderTemp(movePositions[0]);
                        if (movePositions[0] > movePositions[1]) {
                            incomeTypeDao.updateSortOrderDown(movePositions[0], movePositions[1]);
                        } else if (movePositions[0] < movePositions[1]) {
                            incomeTypeDao.updateSortOrderUp(movePositions[0], movePositions[1]);
                        }
                        incomeTypeDao.setSortOrderNewPos(movePositions[1]);
                        break;
                }
            }
        });
    }

    private void addItemTouchHelper() {

        SwipeHandler swipeHandler = new SwipeHandler() {
            @Override
            public void onSwipe(RecyclerView.ViewHolder viewHolder, int direction) {
                sendEndsOfEdition();
                if (direction == ItemTouchHelper.RIGHT) {
                    deleteTypeHandler(viewHolder.getAdapterPosition());
                } else if (direction == ItemTouchHelper.LEFT) {
                    recyclerViewForTypes.getAdapter().notifyItemChanged(viewHolder.getAdapterPosition());
                }
            }
        };
        MoveHandler moveHandler = new MoveHandler() {
            @Override
            public void onMoveEnd(RecyclerView.Adapter adapter) {
                if (adapter instanceof AdapterForRVTypes) {
                    moveSortOrderInDB((AdapterForRVTypes) adapter);
                }
            }
        };

        CallbackForTypes callback = new CallbackForTypes(swipeHandler, moveHandler);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerViewForTypes);

    }

}
