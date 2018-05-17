package ru.hawk_inc.ledlighter.Project;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.KillerBLS.modpeide.document.Document;
import com.KillerBLS.modpeide.document.commons.FileObject;
import com.KillerBLS.modpeide.document.commons.LinesCollection;
import com.KillerBLS.modpeide.processor.TextProcessor;
import com.KillerBLS.modpeide.processor.language.Language;
import com.KillerBLS.modpeide.processor.language.ModPELanguage;
import com.KillerBLS.modpeide.utils.Wrapper;
import com.KillerBLS.modpeide.utils.logger.Logger;
import com.KillerBLS.modpeide.widget.FastScrollerView;
import com.KillerBLS.modpeide.widget.GutterView;

import java.io.Serializable;

import ru.hawk_inc.ledlighter.R;


public class CodeFragment extends Document {
    private TextProcessor mEditor;
    private Project project;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_code, container, false);

        mEditor = view.findViewById(R.id.editor);
        mEditor.init(this);
        mEditor.setCodeCompletion(true);

        mLanguage = new ModPELanguage();
        mText = new Editable.Factory().newEditable("");

        final FastScrollerView mFastScrollerView = view.findViewById(R.id.fast_scroller);
        mFastScrollerView.link(mEditor); //подключаем FastScroller к редактору

        final GutterView mGutterView = view.findViewById(R.id.gutter);
        mGutterView.link(mEditor, this); //подключаем Gutter к редактору

        refreshEditor(); //подключаем все настройки
        setSyntaxHighlight(mWrapper.getSyntaxHighlight());
        mEditor.enableUndoRedoStack(); //включаем Undo/Redo ПОСЛЕ открытия файла
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void saveFile() {
        project.code = mEditor.getText().toString();
        /*if (mEditor != null) {
            try {
                setDirty(false);
            } catch (Exception e) {
                Logger.error(TAG, e);
            }
        } else {
            showToast(getString(R.string.editor_not_found), true);
        }*/
    }

    public void showToast(String message, boolean isError) {
        /*if(isError)
            Toasty.error(getContext(), message, Toast.LENGTH_SHORT).show();
        else
            Toasty.success(getContext(), message, Toast.LENGTH_SHORT).show();*/
    }

    public void setProject(Project project){
        this.project = project;
        mEditor.setText(project.code);
    }
}
