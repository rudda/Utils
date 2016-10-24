package com.github.rudda.utils.Contacts;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by Rudda Beltrao on 13/10/2016.
 */


public class Contact {

    private String number;
    private String nome;
    private String ID;
    private Context com;
    private List<Contact> contatos;
    private String ids[];

    public static Contact build(Context com) {

        return new Contact(com);

    }

    public Contact(Context com) {
        this.com = com;
        contatos = new ArrayList<>();

    }

    public Contact() {
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    private String getTelefone(String id) {

        final String[] projection = {
                ContactsContract.CommonDataKinds.Phone.TYPE,
                ContactsContract.CommonDataKinds.Phone.NUMBER
        };

        // Consulta os telefones filtrando pelo Id do contato.
        Cursor c = com.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                projection,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID +
                        " = ?",
                new String[]{id}, null);
        while (c.moveToNext()) {
            int tipo = c.getInt(c.getColumnIndex(
                    ContactsContract.CommonDataKinds.Phone.TYPE));

            if (tipo == ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE) {

                return c.getString(c.getColumnIndex(
                        ContactsContract.CommonDataKinds.Phone.NUMBER));
            }

        }
        c.close();

        return null;
    }


    public List<Contact> getContatos() {

        Cursor cursor = com.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        int nameIdx = cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME);
        int numberIdx = cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID);

        ids = new String[cursor.getCount()];

        if (cursor.moveToFirst()) {
            do {
                //Extrai o nome
                String name = cursor.getString(nameIdx).toLowerCase();
                //Extrai o ID
                String ID = cursor.getString(numberIdx);

                Contact c = new Contact();
                c.setNumber(getTelefone(ID));
                c.setNome(name);
                c.setID(ID);

                ids[cursor.getPosition()] = name;
                contatos.add(c);

            } while (cursor.moveToNext());
        }


        orderABC();
        return contatos;

    }


    private void orderABC() {

        Arrays.sort(ids);
        List<Contact> aux = new ArrayList<>();

        for (int i = 0; i < ids.length; i++) {

            for (int j = 0; j < ids.length; j++) {

                if (contatos.get(j).getNome().equals(ids[i]))
                    aux.add(contatos.get(j));

            }

        }

        contatos.clear();
        contatos = aux;

    }

}