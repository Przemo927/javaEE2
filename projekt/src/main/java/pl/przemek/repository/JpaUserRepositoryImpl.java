package pl.przemek.repository;

import pl.przemek.model.User;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Stateless
public class JpaUserRepositoryImpl extends JpaRepository<User> implements JpaUserRepository  {

    private final static String PRESENCE_OF_USERNAME="SELECT 1 FROM User WHERE username=:username";
    private final static String PRESENCE_OF_EMAIL="SELECT 1 FROM User WHERE email=:email";

    @Override
    @PermitAll
    public void add(User user) {
    em.persist(user);
	}

    @RolesAllowed("admin")
    @Override
    public Integer updateWithoutPassword(User user) {
        Query queryUpdateUser = em.createNamedQuery("User.editUser");
        queryUpdateUser.setParameter("username", user.getUsername());
        queryUpdateUser.setParameter("email", user.getEmail());
        queryUpdateUser.setParameter("active", user.isActive());
        queryUpdateUser.setParameter("id", user.getId());
        return queryUpdateUser.executeUpdate();
    }

    @RolesAllowed({"admin","user"})
    @Override
    public List<User> getUserByUsername(String username) {
        setInactiveIfLasLoginLAterThan365Days();
        TypedQuery<User> getAllQuery = em.createNamedQuery("User.findByUsername", User.class);
        getAllQuery.setParameter("username",username);
        return getAllQuery.getResultList();

    }
    @PermitAll
    @Override
    public boolean checkPresenceOfUserByUsername(String username){
        Query query=em.createNativeQuery(PRESENCE_OF_USERNAME);
        query.setParameter("username",username);
        List list=query.getResultList();
        return list.size() == 0;
    }
    @PermitAll
    @Override
    public boolean checkPresenceOfEmail(String email){
        Query query=em.createNativeQuery(PRESENCE_OF_EMAIL);
        query.setParameter("email",email);
        List list=query.getResultList();
        return list.size() == 0;
    }
    @RolesAllowed({"admin","user"})
    @Override
    public boolean updateLastLogin(String username){
        Query query=em.createNamedQuery("User.setLastLogin");
        query.setParameter("username",username);
        query.setParameter("lastLogin",new Timestamp(new Date().getTime()));
        return query.executeUpdate() > 0;
    }

    @Override
    @PermitAll
    public int setInactiveIfLasLoginLAterThan365Days() {
        Query query=em.createNamedQuery("User.SetInActive");
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        query.setParameter("date",simpleDateFormat.format(new Date()));
        return query.executeUpdate();
    }
}
