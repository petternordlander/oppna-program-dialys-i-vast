package se.vgregion.dialys.i.vast.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import se.vgregion.dialys.i.vast.jpa.AbstractEntity;
import se.vgregion.dialys.i.vast.jpa.requisitions.User;
import se.vgregion.dialys.i.vast.repository.UserRepository;
import se.vgregion.dialys.i.vast.util.PasswordEncoder;

import javax.naming.CommunicationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.FailedLoginException;
import java.util.Properties;

/**
 * @author Patrik Björk
 */
@Service
public class LdapLoginService {

    @Autowired
    private UserRepository userRepository;

    @Value("${ldap.service.user.dn}")
    private String serviceUserDN;

    @Value("${ldap.service.user.password}")
    private String serviceUserPassword;

    @Value("${ldap.search.base}")
    private String base;

    @Value("${ldap.url}")
    private String ldapUrl;

    @Value("${default.admin.username}")
    private String defaultAdminUsername;

    @Value("${default.admin.encoded.password}")
    private String defaultAdminEncodedPassword;

    private static final Logger LOGGER = LoggerFactory.getLogger(LdapLoginService.class);

    /*public User login(String username, String password) throws FailedLoginException {
        return login(username, password, true);
    }*/

    /*public User loginWithoutPassword(String username) throws FailedLoginException {
        try {
            return login(username, null, false);
        } catch (Exception e) {
            throw new RuntimeException("Could not login with '" + username + "'", e);
            // return userRepository.findOne(username);
        }
    }*/

    /*public User loginOffline(String username) throws FailedLoginException {
        return userRepository.findOne(username);
    }*/

    private User toUser(String username, SearchResult result) throws NamingException {
        // if (true) throw new RuntimeException();
        // generate the users DN (distinguishedName) from the result

        // System.out.println("has more: " + results.hasMore());

        // SearchResult result = results.next();
        String distinguishedName = result.getNameInNamespace();
        User user = new User();
        user.setUserName(username);
        String firstName = ((String) (result).getAttributes().get("givenName").get());
        String lastName = ((String) (result).getAttributes().get("sn").get());
        user.setName(firstName + " " + lastName);
        // String mail = ((String) (result).getAttributes().get("mail").get());
        String displayName = ((String) (result).getAttributes().get("displayName").get());
        user.setName(displayName);
        return user;
    }

    public User login(String username, String password) {
        DirContext serviceCtx = null;
        try {
            // use the service user to authenticate
            serviceCtx = createInitialDirContext();

            NamingEnumeration<SearchResult> results = findUser(username, serviceCtx);

            if (results.hasMore()) {
                // generate the users DN (distinguishedName) from the result
                SearchResult result = results.next();
                String distinguishedName = result.getNameInNamespace();
                verifyPassword(password, distinguishedName);
                User user = toUser(username, result);
                // user = syncUser(user);
                return user;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    /*private User login(String username, String password, boolean verifyPassword) throws FailedLoginException {

        username = username.trim().toLowerCase();

        if (username.equals(defaultAdminUsername)
                && PasswordEncoder.getInstance().matches(password, defaultAdminEncodedPassword)) {

            User user = new User();
            user.setUserName(defaultAdminUsername);
            user.setName("Admin");
            user.setPassWord(password);
            //user.setDisplayName("Admin");
            //user.setRole(Role.ADMIN);
            //TODO: fix roles...

            if (userRepository.findOne(user.getUserName()) == null) {
                userRepository.save(user);
            }

            return user;
        }

        if (userRepository.findOne(username) == null) {
            throw new FailedLoginException("The user is not registered in the application.");
        }

        // first create the service context
        DirContext serviceCtx = null;
        try {
            // use the service user to authenticate
            serviceCtx = createInitialDirContext();

            NamingEnumeration<SearchResult> results = findUser(username, serviceCtx);

            if (results.hasMore()) {
                // generate the users DN (distinguishedName) from the result
                SearchResult result = results.next();
                String distinguishedName = result.getNameInNamespace();

                if (verifyPassword) {
                    verifyPassword(password, distinguishedName);
                }

                User user = toUser(username, result);
                *//*user.setUserName(username);
                String firstName = ((String) (result).getAttributes().get("givenName").get());
                String lastName = ((String) (result).getAttributes().get("sn").get());
                user.setName(firstName + " " + lastName);
                String mail = ((String) (result).getAttributes().get("mail").get());
                String displayName = ((String) (result).getAttributes().get("displayName").get());
                user.setName(displayName);*//*


                user = syncUser(user);

                System.out.println(
                        "Found user in ldap: " + user
                );

                return user;
            } else {
                throw new AccountNotFoundException("Nu user found with given username.");
            }
        } catch (CommunicationException e) {
            throw new RuntimeException(e);
        } catch (AccountNotFoundException e) {
            throw new FailedLoginException(e.getMessage());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new FailedLoginException(e.getMessage());
        } finally {
            if (serviceCtx != null) {
                try {
                    serviceCtx.close();
                } catch (NamingException e) {
                    e.printStackTrace();
                }
            }
        }
    }*/

    private void verifyPassword(String password, String distinguishedName) throws NamingException {
        // attempt another authentication, now with the user
        Properties authEnv = new Properties();
        authEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        authEnv.put(Context.PROVIDER_URL, ldapUrl);
        authEnv.put(Context.SECURITY_PRINCIPAL, distinguishedName);
        authEnv.put(Context.SECURITY_CREDENTIALS, password);
        new InitialDirContext(authEnv);
    }

    public User findUser(String userName) {
        InitialDirContext serviceCtx = null;
        try {
            serviceCtx = createInitialDirContext();
            NamingEnumeration<SearchResult> results = findUser(userName, serviceCtx);
            if (!results.hasMore()) {
                return null;
            }
            return toUser(userName, results.next());
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }

    private NamingEnumeration<SearchResult> findUser(String username, DirContext serviceCtx) throws NamingException {
        String identifyingAttribute = "cn";
        String identifier = username;

        // we don't need all attributes, just let it generate the identifying one
        String[] attributeFilter = {identifyingAttribute, "givenName", "mail", "sn", "displayName", "thumbnailPhoto"};
        SearchControls sc = new SearchControls();
        sc.setReturningAttributes(attributeFilter);
        sc.setSearchScope(SearchControls.SUBTREE_SCOPE);

        // use a search filter to find only the user we want to authenticate
        String searchFilter = "(" + identifyingAttribute + "=" + identifier + ")";
        return serviceCtx.search(base, searchFilter, sc);
    }

    private InitialDirContext createInitialDirContext() throws NamingException {
        Properties serviceEnv = new Properties();
        serviceEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        serviceEnv.put(Context.PROVIDER_URL, ldapUrl);
        serviceEnv.put(Context.SECURITY_AUTHENTICATION, "simple");
        serviceEnv.put(Context.SECURITY_PRINCIPAL, serviceUserDN);
        serviceEnv.put(Context.SECURITY_CREDENTIALS, serviceUserPassword);
        serviceEnv.put("com.sun.jndi.ldap.connect.timeout", "5000");

        return new InitialDirContext(serviceEnv);
    }

    /*private User syncUser(User user) {
        User foundUser = userRepository.findOne(user.getUserName());
        if (foundUser != null) {
            if (!AbstractEntity.equals(user.getName(), foundUser.getName())) {
                user.setName(foundUser.getName());
                return userRepository.save(user);
            }
            return user;
        } else {
            throw new RuntimeException("Only persisted users should be able to login.");
        }
    }*/

}
