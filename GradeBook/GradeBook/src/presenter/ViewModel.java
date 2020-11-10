/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package presenter;

import java.util.List;
import model.Student;

/**
 *
 * @author PC
 */
public class ViewModel {

    List<Student> model;
    Student current;
    int index;
    int n;

    ViewModel() {
    }

    void set(List<Student> m) {
        model = m;
        index = 0;
        n = model.size();
        current = model.get(index);
    }

    IndexedStudent previous() {
        if (--index < 0) {
            index = n - 1;
        }
        return new IndexedStudent(model.get(index), index + 1, n);
    }

    IndexedStudent next() {
        if (++index > n - 1) {
            index = 0;
        }
        return new IndexedStudent(model.get(index), index + 1, n);
    }

    IndexedStudent current() {
        return new IndexedStudent(model.get(index), index + 1, n);
    }
}
