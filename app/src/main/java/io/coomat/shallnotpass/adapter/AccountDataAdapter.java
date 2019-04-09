package io.coomat.shallnotpass.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.codecrafters.tableview.TableDataAdapter;
import io.coomat.shallnotpass.model.Account;

public class AccountDataAdapter extends TableDataAdapter<Account> {

    private int paddingLeft = 20;
    private int paddingTop = 15;
    private int paddingRight = 20;
    private int paddingBottom = 15;
    private int textSize = 18;
    private int typeface = Typeface.NORMAL;
    private int textColor = 0x99000000;

    public AccountDataAdapter(Context context, List<Account> data) {
        super(context, data);
    }

    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        Account account = getRowData(rowIndex);

        return getColView(columnIndex, account);
    }

    private View getColView(int colIndex, Account account) {
        final TextView textView = new TextView(getContext());
        textView.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        textView.setTypeface(textView.getTypeface(), typeface);
        textView.setTextSize(textSize);
        textView.setTextColor(textColor);
        textView.setSingleLine();
        textView.setEllipsize(TextUtils.TruncateAt.END);

        switch(colIndex) {
            case 0:
                textView.setText(account.getSite());
                break;
            case 1:
                textView.setText(account.getUsername());
                break;
            case 2:
                textView.setText(account.getPassword());
                break;
            default:
                break;
        }

        return textView;
    }
}
