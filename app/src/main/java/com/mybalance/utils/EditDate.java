package com.mybalance.utils;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

public class EditDate extends AppCompatEditText {

    int day;
    int month;
    int year;
    char[] chars = new char[10];
    // boolean dateIsSet = false;
    String prevString;
    private boolean isUpdatingText = false;

    OnDateChange onDateChangeListener;

    public void addOnDateChangeListener(EditDate.OnDateChange listener) {
        onDateChangeListener = listener;
    }

    private void callListener() {
        if (onDateChangeListener != null) {
            onDateChangeListener.onChange(day, month, year);
        }
    }

    public EditDate(@NonNull Context context) {
        super(context);
        init();
    }

    public EditDate(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EditDate(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        // it closes method super.setText
    }

    private void setTextCustom(CharSequence text) {
        super.setText(text, BufferType.NORMAL);
    }

    public boolean setDateStr(@NonNull String date) {
        int[] a = new int[10];
        if (date.length() < 10) {
            return false;
        }
        for (int i = 0; i < 10; i++) {
            if (i == 4 || i == 7) {
                continue;
            }
            a[i] = date.charAt(i) - 48;
            if (a[i] < 0 || a[i] > 9) {
                return false;
            }
        }
        int yearInput = a[0] * 1000 + a[1] * 100 + a[2] * 10 + a[3];
        int monthInput = a[5] * 10 + a[6];
        int dayInput = a[8] * 10 + a[9];
        boolean dateChanged = false;
        if (this.year != yearInput) {
            this.year = checkYear(yearInput);
            dateChanged = true;
        }
        if (this.month != monthInput) {
            this.month = checkMonth(monthInput);
            dateChanged = true;
        }
        if (this.day != dayInput) {
            this.day = checkDay(dayInput);
            dateChanged = true;
        }

        this.year = checkYear(yearInput);
        this.month = checkMonth(monthInput);
        this.day = checkDay(dayInput);
        makeYearChars();
        makeMonthChars();
        makeDayChars();
        setTextCustom(new String(chars));
        if (dateChanged) {
            callListener();
        }
        return true;
    }

    public void setDateInt(int dd, int mm, int yyyy) {
        boolean dateChanged = false;
        if (year != yyyy) {
            year = checkYear(yyyy);
            dateChanged = true;
        }
        if (month != mm) {
            month = checkMonth(mm);
            dateChanged = true;
        }
        if (day != dd) {
            day = checkDay(dd);
            dateChanged = true;
        }
        day = checkDay(dd);
        makeYearChars();
        makeMonthChars();
        makeDayChars();
        String date = new String(chars);
        setTextCustom(date);
        if (dateChanged) {
            callListener();
        }
    }

    @Nullable
    public String getDate() {
        if (day==0 || year==0|| month==0){
            return null;
        }
        makeDayChars();
        makeYearChars();
        makeMonthChars();
        char[] dateChars = {chars[6], chars[7], chars[8], chars[9], '-', chars[3], chars[4], '-', chars[0], chars[1]};
        return new String(dateChars);
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    private void init() {
        setHint("dd.mm.yyyy");
        chars[0] = ' ';
        chars[1] = ' ';
        chars[2] = '.';
        chars[3] = ' ';
        chars[4] = ' ';
        chars[5] = '.';
        chars[6] = ' ';
        chars[7] = ' ';
        chars[8] = ' ';
        chars[9] = ' ';
        setListeners();
    }

    private void setListeners() {
        this.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (isUpdatingText) return;
                isUpdatingText = true;
                prevString = s.toString();
                isUpdatingText = false;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isUpdatingText) return;
                isUpdatingText = true;
                inputsHandler(s, start, before, count);
                isUpdatingText = false;
            }

            @Override
            public void afterTextChanged(Editable s) {
                // This Methode is useless in the context
            }
        });


        setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (isUpdatingText) {
                    return;
                }
                isUpdatingText = true;
                if (hasFocus && getText() != null) {
                    if (getText().length() < 1) {
                        setTextCustom("  .  .    ");
                        setSelection(0);
                    }
                }

                isUpdatingText = false;
            }
        });

    }

    private void inputsHandler(CharSequence cha, int position, int deleted, int add) {
        if (position > 9) {
            makeYear();
            setTextCustom(new String(chars));
            setSelection(10);
            return;
        }
        if (deleted > 0) {
            if (position == 2) {
                setTextCustom(prevString);
                setSelection(1);
            } else if (position == 5) {
                setTextCustom(prevString);
                setSelection(4);
            } else {
                chars[position] = ' ';
            }
            String str = new String(chars);
            setTextCustom(str);
            setSelection(position);

        }
        if (add > 0) {
            int corPosition = 1;
            if (cha.charAt(position) == '.' || cha.charAt(position) == ',') {
                if (position < 2) {
                    corPosition++;
                    makeDay();
                } else if (position < 5) {
                    corPosition++;
                    makeMonth();
                }

            } else {

                if (position == 2) {
                    chars[3] = cha.charAt(position);
                    corPosition++;
                } else if (position == 5) {
                    chars[6] = cha.charAt(position);
                    corPosition++;
                } else {
                    chars[position] = cha.charAt(position);
                }
                if (position == 1) {
                    corPosition++;
                    makeDay();
                }
                if (position == 4) {
                    corPosition++;
                    makeMonth();
                }
                if (position == 9) {
                    makeYear();
                }
            }

            String str = new String(chars);
            setTextCustom(str);
            setSelection(position + corPosition);

        }
    }

    private void makeDay() {
        int a1;
        int a0;
        if (chars[0] == ' ') {
            a0 = 0;
        } else {
            a0 = chars[0] - 48;
        }
        if (chars[1] == ' ') {
            a1 = a0;
            a0 = 0;
        } else {
            a1 = chars[1] - 48;
        }
        int dayInput = a0 * 10 + a1;

        this.day = checkDay(dayInput);
        makeDayChars();
        callListener();
    }

    private void makeMonth() {
        int a1;
        int a0;
        if (chars[3] == ' ') {
            a0 = 0;
        } else {
            a0 = chars[3] - 48;
        }
        if (chars[4] == ' ') {
            a1 = a0;
            a0 = 0;
        } else {
            a1 = chars[4] - 48;
        }
        int monthInput = a0 * 10 + a1;
        monthInput = checkMonth(monthInput);
        this.month = monthInput;
        day = checkDay(day);
        makeDayChars();
        makeMonthChars();
        callListener();
    }

    private void makeYear() {
        int[] a = new int[4];
        for (int i = 0; i < 4; i++) {
            if (chars[i + 6] == ' ') {
                a[i] = 0;
            } else {
                a[i] = chars[i + 6] - 48;
            }
        }
        int yearInput = a[0] * 1000 + a[1] * 100 + a[2] * 10 + a[3];
        yearInput = checkYear(yearInput);
        this.year = yearInput;
        day = checkDay(day);
        makeYearChars();
        makeDayChars();
        callListener();
    }

    private int checkMonth(int month) {
        if (month > 12) {
            month = 12;
        }
        if (month < 1) {
            month = 1;
        }
        return month;
    }

    private int checkYear(int year) {
        if (year > 9000) {
            year = 9000;
        }
        if (year < 1) {
            year = 1;
        }
        return year;
    }

    private int checkDay(int day) {
        int dayout = day;
        if (dayout < 1) {
            dayout = 1;
        }
        switch (month) {
            case 4:
            case 6:
            case 9:
            case 11:
                if (dayout > 30)
                    dayout = 30;
                break;

            case 2:
                if (dayout >= 29) {
                    if (isLeapYear(year)) {
                        dayout = 29;
                    } else {
                        dayout = 28;
                    }
                }
                break;
            default:
                if (dayout > 31) {
                    dayout = 31;
                }
                break;
        }
        return dayout;
    }

    private void makeMonthChars() {
        int a0 = month / 10;
        int a1 = month % 10;
        chars[3] = (char) (a0 + 48);
        chars[4] = (char) ('0' + a1);
    }

    private void makeDayChars() {
        int a0 = day / 10;
        int a1 = day % 10;
        chars[0] = (char) (a0 + 48);
        chars[1] = (char) ('0' + a1);
    }

    private void makeYearChars() {
        int[] a = new int[4];
        a[0] = year / 1000;
        int rest = year % 1000;
        a[1] = rest / 100;
        rest = rest % 100;
        a[2] = rest / 10;
        a[3] = rest % 10;
        for (int i = 0; i < 4; i++) {
            chars[i + 6] = (char) (a[i] + 48);
        }
    }

    private boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    public interface OnDateChange {
        public void onChange(int day, int month, int year);
    }
}


