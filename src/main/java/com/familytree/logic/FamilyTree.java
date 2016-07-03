package com.familytree.logic;

import com.familytree.dao.DBStorage;
import com.familytree.model.AllMembers;
import com.familytree.model.Member;
import com.familytree.model.ResponseTree;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Scanner;

@Component
public class FamilyTree {
    private static Scanner scan = new Scanner(System.in);
    @Resource
    DBStorage db;

    public void init() {
        System.out.println("started");
        db.readDataBase();

        if (AllMembers.isEmpty()) {
            System.out.println("This is the first run... let us get GOD");
            Member god = new Member();
            god.setGender('m');
            god.setLevel("#");
            god.setName("god");
            god.setNickName("god");
            db.addGod(god);
            AllMembers.put(god.getId(), god);
            AllMembers.godId = god.getId();
            System.out.println("Let the games begin...\n");

        } else {
            AllMembers.godId = AllMembers.get("god").getId();
        }

    }

    public ResponseTree getRoot() {
        Member root = AllMembers.get(AllMembers.godId);
        ResponseTree resp = new ResponseTree();
        return getResponseObject(root, resp);

    }

    private ResponseTree getResponseObject(Member root, ResponseTree resp) {

        BeanUtils.copyProperties(root, resp, "spouse", "children");
        resp.setChildren(new ArrayList<ResponseTree>());
        for (Member m : root.children) {
            ResponseTree r = new ResponseTree();
            if (m.getChildren() != null && !m.getChildren().isEmpty()) {
                r = getResponseObject(m, r);
            } else {
                BeanUtils.copyProperties(m, r, "spouse", "children");
            }

            resp.getChildren().add(r);

        }
        return resp;
    }

    private void displayTree() {
        Member root = AllMembers.get(AllMembers.godId);
        printChildren(root, -1);
    }

    private void print(String name, int l) {
        for (int i = 0; i < l; i++) {
            System.out.print("  ");
        }
        System.out.print(name);
    }

    private void printChildren(Member c, int l) {
        l++;

        for (Member m : c.children) {
            System.out.println();
            print(m.getNickName(), l);
            if (m.getSpouse() != null)
                System.out.print("-" + m.getSpouse().getNickName());
            if (!m.children.isEmpty()) {
                printChildren(m, l);
            }

        }

    }

    private void deleteMember() {
        System.out.println("Deleting Member, nickName please");
        String nickName = scan.next();

		/*
         * TODO
		 * delete from db also
		 */

    }

    private void showDetail() {
        System.out.println(" nickName please");
        String nickName = scan.next();
        Member m = AllMembers.get(nickName);
        System.out.println("--Details --");
        System.out.println("Name: " + m.getName());
        System.out.println("nickName: " + m.getNickName());
        System.out.println("hierarchy: " + m.getLevel());
        System.out.println("gender: " + m.getGender());
        System.out.println("Spouse" + m.getSpouse() == null ? "not-married" : m.getSpouse().getName());
        System.out.println("Father: " + m.getParent().getName());
        System.out.println("Mother: " + m.getParent().getSpouse().getName());
    }

    private void editMember() {
        System.out.println(" nickName please");
        String nickName = scan.next();
        Member m = AllMembers.get(nickName);
        System.out.println("--- Edit Member ---");
        System.out.println("1. Father");
        System.out.println("2. Spouse");
        System.out.println("3. Name");
        System.out.println("4. gender");
        int choice = scan.nextInt();
        if (choice == 1) {
            Member oldFather = m.getParent();
            if (oldFather != null) {
                System.out.println("Father's nickName : " + oldFather.getNickName());
                oldFather.children.remove(m);
            }
            System.out.println("Change to father with nickName: ");
            String fatherNn = scan.next();
            Member father = AllMembers.get(fatherNn);
            if (father == null) {
                System.out.println("No such member found");
            } else {
                m.setParent(father);
                m.setLevel(father.getLevel() + "_" + (father.children.size() + 1));
                father.children.add(m);
                db.changeFather(m, father);
            }
        } else if (choice == 2) {
            Member oldSpouse = m.getSpouse();
            if (oldSpouse != null) {
                oldSpouse.setSpouse(null);
                System.out.println("Spouse nickName : " + oldSpouse.getNickName());
            }
            System.out.println("Change to Spouse with nickName: ");
            String spouseNn = scan.next();
            Member spouse = AllMembers.get(spouseNn);
            if (spouse == null) {
                System.out.println("No such member found");
            } else {
                m.setSpouse(spouse);
                spouse.setSpouse(m);
                db.changeSpouse(m, spouse);
            }
        } else if (choice == 3) {
            System.out.println("Name : " + m.getName());
            System.out.println("Enter new name:");
            String newName = scan.next();
            m.setName(newName);
            db.updateMember(m);
        } else if (choice == 4) {
            System.out.println("Current gender : " + m.getGender());
            System.out.println("Do you want to change it 1/0");
            int i = scan.nextInt();
            if (i == 1) {
                if (m.getGender() == 'm') {
                    m.setGender('f');
                } else {
                    m.setGender('m');
                }
                db.updateMember(m);
            }

        }

    }

    private void findRelation() {
        System.out.println("1st nickname please");
        String id1 = scan.next();
        System.out.println("2nd nickname please");
        String id2 = scan.next();
        Member n1 = AllMembers.get(id1);
        Member n2 = AllMembers.get(id2);
        if (n1.getSpouse() == n2 || n2.getSpouse() == n1) {
            System.out.println("They are husband wife");
        } else {
            String l1 = n1.getLevel();
            if (l1 == null) {
                if (n1.getSpouse() != null)
                    l1 = n1.getSpouse().getLevel();
                else {
                    System.out.println("no level defined for n1");
                    return;
                }
            }
            String l2 = n2.getLevel();
            if (l2 == null) {
                if (n2.getSpouse() != null)
                    l2 = n2.getSpouse().getLevel();
                else {
                    System.out.println("no level defined for n2");
                    return;
                }
            }
            String[] l1arr = l1.split("_");
            String[] l2arr = l2.split("_");
            //String last="";
            int c = 0;

            if (l1arr.length == l2arr.length) {
                for (int i = 0; i < l1arr.length; i++) {
                    if (!l1arr[i].equals(l2arr[i])) {
                        c++;
                    }
                }
                if (c == 1) {
                    System.out.println("They are siblings");
                } else if (c == 2) {
                    System.out.println("They are cousins");
                } else if (c == 3) {
                    System.out.println("They have same grandparent");
                }
            }
            if (l1.contains(l2)) {
                c = l1arr.length - l2arr.length;
                if (c == 1) {
                    System.out.println("first 1 is parent of 2nd one");
                } else if (c == 2) {
                    System.out.println("first 1 is grand-parent of 2nd one");
                }
            } else if (l2.contains(l1)) {
                c = l2arr.length - l1arr.length;
                if (c == 1) {
                    System.out.println("2nd is parent of 1st one");
                } else if (c == 2) {
                    System.out.println("2nd is grand-parent of 1st one");
                }
            }

        }

    }

    private void addMember() {
        System.out.println("Adding Member, nickName please");
        String nickName = scan.next();
        System.out.println("name please");
        String name = scan.next();

        System.out.println("Is this male:");
        int isMale = scan.nextInt();
        char gender = 'm';
        char spouseGender = 'f';
        if (isMale == 0) {
            gender = 'f';
            spouseGender = 'm';
        }
        System.out.println("fathers nickname:");
        String fatherNickName = scan.next();

        Member father = AllMembers.get(fatherNickName);
        Member m = new Member();
        m.setNickName(nickName);
        m.setName(name);
        m.setParent(father);
        m.setGender(gender);
        m.setLevel(father.getLevel() + "_" + (father.children.size() + 1));

        //members.put(nickName, m);


        father.children.add(m);
        System.out.println("married 1/0");
        int married = scan.nextInt();

        if (married == 1) {
            System.out.println("Ok, Spouse nickname : ");
            String spouseNickName = scan.next();
            Member spouse = AllMembers.get(spouseNickName);
            if (spouse != null) {
                spouse.setSpouse(m);
                m.setSpouse(spouse);
                db.addMember(m);
            } else {
                spouse = new Member();
                spouse.setNickName(spouseNickName);
                spouse.setGender(spouseGender);
                spouse.setSpouse(m);
                Member spouseParent = AllMembers.get(AllMembers.godId);
                spouse.setParent(spouseParent);
                spouse.setLevel(spouseParent.getLevel() + "_" + (spouseParent.children.size() + 1));

                m.setSpouse(spouse);

                db.addMember(spouse);
                db.addMember(m);
            }
            if (gender == 'm') {
                db.addCouple(m, spouse);
            } else {
                db.addCouple(spouse, m);
            }
            AllMembers.put(m.getId(), m);
            AllMembers.put(spouse.getId(), spouse);
            AllMembers.get(AllMembers.godId).children.add(spouse);
        } else {
            db.addMember(m);
            AllMembers.put(m.getId(), m);
        }

    }
}
