package com.example.pr12_zubarev;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText titleInput = findViewById(R.id.title_input);
        EditText authorInput = findViewById(R.id.author_input);
        Button saveButton = findViewById(R.id.save_button);
        Button deleteButton = findViewById(R.id.delete_button);
        Button findButton = findViewById(R.id.find_button);
        Button task2Button = findViewById(R.id.activity2goto);
        RecyclerView booksList = findViewById(R.id.books_list);
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        List<Book> books = dbHelper.getAllBooks();
        BookAdapter adapter = new BookAdapter((ArrayList<Book>) books);
        booksList.setLayoutManager(new LinearLayoutManager(this));
        booksList.setAdapter(adapter);

        saveButton.setOnClickListener(v -> {
            String title = titleInput.getText().toString();
            String author = authorInput.getText().toString();
            if (dbHelper.addBook(new Book(0, title, author))) {
                books.add(new Book(0, title, author));
                adapter.notifyItemInserted(books.size() - 1);
                Toast.makeText(this, "Book saved successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to save book", Toast.LENGTH_SHORT).show();
            }
        });

        task2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
                startActivity(intent);
            }
        });

        deleteButton.setOnClickListener(v -> {
            String author = authorInput.getText().toString();
            if (dbHelper.deleteBook(author)) {
                int position = -1;
                for (int i = 0; i < books.size(); i++) {
                    if (books.get(i).getAuthor().equals(author)) {
                        position = i;
                        books.remove(i);
                        break;
                    }
                }
                if (position != -1) {
                    adapter.notifyItemRemoved(position);
                    Toast.makeText(this, "Book deleted successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Book not found", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Failed to delete book", Toast.LENGTH_SHORT).show();
            }
        });

        findButton.setOnClickListener(v -> {
            String author = authorInput.getText().toString();
            Book foundBook = dbHelper.findBook(author);
            if (foundBook != null) {
                titleInput.setText(foundBook.getTitle());
                authorInput.setText(foundBook.getAuthor());
                Toast.makeText(this, "Book found: " + foundBook.getTitle(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Book not found", Toast.LENGTH_SHORT).show();
            }
        });

        Button updateButton = findViewById(R.id.update_button);
        updateButton.setOnClickListener(v -> {
            String oldAuthor = authorInput.getText().toString();
            String newTitle = titleInput.getText().toString();
            String newAuthor = authorInput.getText().toString();
            if (dbHelper.updateBook(oldAuthor, newTitle, newAuthor)) {
                Toast.makeText(this, "Book updated successfully!", Toast.LENGTH_SHORT).show();
                refreshBooksList(dbHelper, books, adapter, booksList);
            } else {
                Toast.makeText(this, "Failed to update book", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void refreshBooksList(DatabaseHelper dbHelper,
                                  List<Book> books, BookAdapter adapter, RecyclerView
                                          booksList) {
        books = dbHelper.getAllBooks();
        adapter = new BookAdapter((ArrayList<Book>) books);
        booksList.setAdapter(adapter);
    }
}
