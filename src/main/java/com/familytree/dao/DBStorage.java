package com.familytree.dao;

import com.familytree.model.AllMembers;
import com.familytree.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

@Repository
public class DBStorage {
    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
    @Autowired
    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void addGod(Member member) {
        try {
            connect = dataSource.getConnection();
            statement = connect.createStatement();
            int gender = 1;
            if (member.getGender() == 'f')
                gender = 0;
            long i = statement.executeUpdate("insert into members(nickname,name,level,gender) values('" + member.getNickName() + "','" + member.getName() + "','" + member.getLevel() + "'," + gender + ")", Statement.RETURN_GENERATED_KEYS);
            if (i == 1) {
                ResultSet r = statement.getGeneratedKeys();
                r.next();
                member.setId(r.getLong(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connect.close();
            } catch (Exception e) {
            }
        }

    }

    public void addMember(Member member) {
        try {
            connect = dataSource.getConnection();
            statement = connect.createStatement();
            int gender = 1;
            if (member.getGender() == 'f')
                gender = 0;
            long parentId = member.getParent() == null ? AllMembers.godId : member.getParent().getId();
            long i = statement.executeUpdate("insert into members(nickname,name,level,gender) values('" + member.getNickName() + "','" + member.getName() + "','" + member.getLevel() + "'," + gender + ")", Statement.RETURN_GENERATED_KEYS);
            if (i == 1) {
                ResultSet r = statement.getGeneratedKeys();
                r.next();
                member.setId(r.getLong(1));
                System.out.println("insert into parent_mapping(parent_id,child_id) values(" + parentId + "," + member.getId());
                long p = statement.executeUpdate("insert into parent_mapping(parent_id,child_id) values(" + parentId + "," + member.getId() + ")");
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connect.close();
            } catch (Exception e) {
            }
        }

    }

    public void addCouple(Member h, Member w) {
        try {
            connect = dataSource.getConnection();
            statement = connect.createStatement();

            long r = statement.executeUpdate("insert into spouse_mapping(husband_id,wife_id) values(" + h.getId() + "," + w.getId() + ")");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connect.close();
            } catch (Exception e) {
            }
        }

    }

    public void readDataBase() {
        try {
            connect = dataSource.getConnection();
            statement = connect.createStatement();
            // Result set get the result of the SQL query
            resultSet = statement
                    .executeQuery("select * from members");
            while (resultSet.next()) {
                Member m = new Member();
                if (resultSet.getInt("gender") == 1)
                    m.setGender('m');
                else
                    m.setGender('f');
                m.setId(resultSet.getLong("memberid"));
                m.setLevel(resultSet.getString("level"));
                m.setName(resultSet.getString("name"));
                m.setNickName(resultSet.getString("nickname"));
                AllMembers.put(m.getId(), m);
                //m.setParent(FamilyTree.members.get(resultSet.getLong("parent_id")));
            }
            resultSet = statement
                    .executeQuery("select * from parent_mapping");
            while (resultSet.next()) {
                Member m = AllMembers.get(resultSet.getLong("child_id"));
                Member parent = AllMembers.get(resultSet.getLong("parent_id"));
                m.setParent(parent);
                parent.children.add(m);
            }
            resultSet = statement
                    .executeQuery("select * from spouse_mapping");
            while (resultSet.next()) {
                Member husband = AllMembers.get(resultSet.getLong("husband_id"));
                Member wife = AllMembers.get(resultSet.getLong("wife_id"));
                husband.setSpouse(wife);
                wife.setSpouse(husband);
            }
            System.out.println(AllMembers.showAll());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connect.close();
            } catch (Exception e) {
            }
        }

    }

    public void changeSpouse(Member member, Member spouse) {
        try {
            connect = dataSource.getConnection();
            statement = connect.createStatement();
            if (member.getGender() == 'm')
                statement.executeUpdate("update spouse_mapping set wife_id=" + spouse.getId() + " where husband_id=" + member.getId());
            else
                statement.executeUpdate("update spouse_mapping set husband_id=" + spouse.getId() + " where wife_id=" + member.getId());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connect.close();
            } catch (Exception e) {
            }
        }
    }

    public void updateMember(Member member) {
        try {
            connect = dataSource.getConnection();
            statement = connect.createStatement();
            int gender = 1;
            if (member.getGender() == 'f')
                gender = 0;
            statement.executeUpdate("update members set nickname='" + member.getNickName() + "',name='" + member.getName() + "',level='" + member.getLevel() + "',gender=" + gender + " where memberid=" + member.getId());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connect.close();
            } catch (Exception e) {
            }
        }
    }

    public void changeFather(Member member, Member father) {
        try {
            connect = dataSource.getConnection();
            statement = connect.createStatement();
            statement.executeUpdate("update parent_mapping set parent_id=" + father.getId() + " where child_id=" + member.getId());
            statement.executeUpdate("update members set level= '" + member.getLevel() + "' where memberid=" + member.getId());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connect.close();
            } catch (Exception e) {
            }
        }
    }

} 