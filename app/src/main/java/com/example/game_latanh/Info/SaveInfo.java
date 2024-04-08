package com.example.game_latanh.Info;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.example.game_latanh.AICore.KnowledgeBase;
import com.example.game_latanh.AICore.TransformTable;


public class SaveInfo {
    public static void SerializeChessInfo(ChessInfo chessInfo, String name) throws Exception, IOException {
        File rootDir = Environment.getExternalStorageDirectory();
        File targetDir = new File(rootDir, "ChessGame");
        if (!targetDir.exists()) {
            targetDir.mkdir();
        }
        ObjectOutputStream oo = new ObjectOutputStream(new FileOutputStream(new File(targetDir, name)));
        oo.writeObject(chessInfo);
        oo.close();
    }

    public static ChessInfo DeserializeChessInfo(String name) throws Exception, IOException {
        File rootDir = Environment.getExternalStorageDirectory();
        File targetDir = new File(rootDir, "ChessGame");
        if (!targetDir.exists()) {
            targetDir.mkdir();
        }
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(targetDir, name)));
        ChessInfo chessInfo = (ChessInfo) ois.readObject();
        return chessInfo;
    }

    public static void SerializeInfoSet(InfoSet infoSet, String name) throws FileNotFoundException, IOException {
        File rootDir = Environment.getExternalStorageDirectory();
        File targetDir = new File(rootDir, "ChessGame");
        if (!targetDir.exists()) {
            targetDir.mkdir();
        }
        ObjectOutputStream oo = new ObjectOutputStream(new FileOutputStream(new File(targetDir, name)));
        oo.writeObject(infoSet);
        oo.close();
    }

    public static InfoSet DeserializeInfoSet(String name) throws Exception, IOException {
        File rootDir = Environment.getExternalStorageDirectory();
        File targetDir = new File(rootDir, "ChessGame");
        if (!targetDir.exists()) {
            targetDir.mkdir();
        }
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(targetDir, name)));
        InfoSet infoSet = (InfoSet) ois.readObject();
        return infoSet;
    }

    public static void SerializeKnowledgeBase(KnowledgeBase knowledgeBase, String name) throws Exception, IOException {
        File rootDir = Environment.getExternalStorageDirectory();
        File targetDir = new File(rootDir, "ChessGame");
        if (!targetDir.exists()) {
            targetDir.mkdir();
        }
        ObjectOutputStream oo = new ObjectOutputStream(new FileOutputStream(new File(targetDir, name)));
        oo.writeObject(knowledgeBase);
        oo.close();
    }

    public static KnowledgeBase DeserializeKnowledgeBase(String name) throws Exception, IOException {
        File rootDir = Environment.getExternalStorageDirectory();
        File targetDir = new File(rootDir, "ChessGame");
        if (!targetDir.exists()) {
            targetDir.mkdir();
        }
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(targetDir, name)));
        KnowledgeBase knowledgeBase = (KnowledgeBase) ois.readObject();
        return knowledgeBase;
    }

    public static void SerializeTransformTable(TransformTable transformTable, String name) throws Exception, IOException {
        File rootDir = Environment.getExternalStorageDirectory();
        File targetDir = new File(rootDir, "ChessGame");
        if (!targetDir.exists()) {
            targetDir.mkdir();
        }
        ObjectOutputStream oo = new ObjectOutputStream(new FileOutputStream(new File(targetDir, name)));
        oo.writeObject(transformTable);
        oo.close();
    }

    public static TransformTable DeserializeTransformTable(String name) throws Exception, IOException {
        File rootDir = Environment.getExternalStorageDirectory();
        File targetDir = new File(rootDir, "ChessGame");
        if (!targetDir.exists()) {
            targetDir.mkdir();
        }
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(targetDir, name)));
        TransformTable transformTable = (TransformTable) ois.readObject();
        return transformTable;
    }

    public static boolean fileIsExists(String strFile) {
        try {
            File rootDir = Environment.getExternalStorageDirectory();
            File targetDir = new File(rootDir, "ChessGame");
            File f = new File(targetDir, strFile);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
