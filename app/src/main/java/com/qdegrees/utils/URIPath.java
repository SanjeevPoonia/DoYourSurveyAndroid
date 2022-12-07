package com.qdegrees.utils;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;

import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Regex;
import kotlin.text.StringsKt;

public class URIPath {

    @Nullable
    public static String getPath(@NotNull Context context, @NotNull Uri uri) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(uri, "uri");
        boolean isKitKatorAbove = Build.VERSION.SDK_INT >= 19;
        if (isKitKatorAbove && DocumentsContract.isDocumentUri(context, uri)) {
            String docId;
            String[] split;
            CharSequence var6;
            String var7;
            byte var8;
            Collection $this$toTypedArray$iv;
            String type;
            Regex var13;
            boolean $i$f$toTypedArray;
            Object[] var10000;
            if (isExternalStorageDocument(uri)) {
                docId = DocumentsContract.getDocumentId(uri);
                Intrinsics.checkNotNullExpressionValue(docId, "docId");
                var6 = (CharSequence)docId;
                var7 = ":";
                var13 = new Regex(var7);
                var8 = 0;
                $this$toTypedArray$iv = (Collection)var13.split(var6, var8);
                $i$f$toTypedArray = false;
                var10000 = $this$toTypedArray$iv.toArray(new String[0]);
                if (var10000 == null) {
                    throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T>");
                }

                split = (String[])var10000;
                type = split[0];
                if (StringsKt.equals("primary", type, true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1];
                }
            } else {
                if (isDownloadsDocument(uri)) {
                    docId = DocumentsContract.getDocumentId(uri);
                    Uri var17 = Uri.parse("content://downloads/public_downloads");
                    Long var10001 = Long.valueOf(docId);
                    Intrinsics.checkNotNullExpressionValue(var10001, "java.lang.Long.valueOf(id)");
                    var17 = ContentUris.withAppendedId(var17, var10001);
                    Intrinsics.checkNotNullExpressionValue(var17, "ContentUris.withAppended…va.lang.Long.valueOf(id))");
                    Uri contentUri = var17;
                    return getDataColumn(context, contentUri, (String)null, (String[])null);
                }

                if (isMediaDocument(uri)) {
                    docId = DocumentsContract.getDocumentId(uri);
                    Intrinsics.checkNotNullExpressionValue(docId, "docId");
                    var6 = (CharSequence)docId;
                    var7 = ":";
                    var13 = new Regex(var7);
                    var8 = 0;
                    $this$toTypedArray$iv = (Collection)var13.split(var6, var8);
                    $i$f$toTypedArray = false;
                    var10000 = $this$toTypedArray$iv.toArray(new String[0]);
                    if (var10000 == null) {
                        throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T>");
                    }

                    split = (String[])var10000;
                    type = split[0];
                    Uri contentUri = (Uri)null;
                    if (Intrinsics.areEqual("image", type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if (Intrinsics.areEqual("video", type)) {
                        contentUri = android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if (Intrinsics.areEqual("audio", type)) {
                        contentUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    String selection = "_id=?";
                    String[] selectionArgs = new String[]{split[1]};
                    return getDataColumn(context, contentUri, selection, selectionArgs);
                }
            }
        } else {
            if (StringsKt.equals("content", uri.getScheme(), true)) {
                return getDataColumn(context, uri, (String)null, (String[])null);
            }

            if (StringsKt.equals("file", uri.getScheme(), true)) {
                return uri.getPath();
            }
        }

        return null;
    }

    @Nullable
    public static String getDataColumn(@NotNull Context context, @Nullable Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        Intrinsics.checkNotNullParameter(context, "context");
        Cursor cursor = (Cursor)null;
        String column = "_data";
        String[] projection = new String[]{column};
        boolean var12 = false;

        String var9;
        label79: {
            try {
                var12 = true;
                Cursor var10000;
                if (uri != null) {
                    boolean var10 = false;
                    var10000 = context.getContentResolver().query(uri, projection, selection, selectionArgs, (String)null);
                } else {
                    var10000 = null;
                }

                cursor = var10000;
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        int column_index = cursor.getColumnIndexOrThrow(column);
                        var9 = cursor.getString(column_index);
                        var12 = false;
                        break label79;
                    }

                    var12 = false;
                } else {
                    var12 = false;
                }
            } finally {
                if (var12) {
                    if (cursor != null) {
                        cursor.close();
                    }

                }
            }

            if (cursor != null) {
                cursor.close();
            }

            return null;
        }

        cursor.close();
        return var9;
    }

    public static boolean isExternalStorageDocument(@NotNull Uri uri) {
        Intrinsics.checkNotNullParameter(uri, "uri");
        return Intrinsics.areEqual("com.android.externalstorage.documents", uri.getAuthority());
    }

    public static boolean isDownloadsDocument(@NotNull Uri uri) {
        Intrinsics.checkNotNullParameter(uri, "uri");
        return Intrinsics.areEqual("com.android.providers.downloads.documents", uri.getAuthority());
    }

    public static boolean isMediaDocument(@NotNull Uri uri) {
        Intrinsics.checkNotNullParameter(uri, "uri");
        return Intrinsics.areEqual("com.android.providers.media.documents", uri.getAuthority());
    }
}
