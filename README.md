# MultiStore
> Information system integrated with account models of various social networking services (Used systems are Google and Facebook). The main aim of system is supporting of internet bookstore. It was achieved by implementing transactional processing.

## Table of contents
* [Screenshots](#screenshots)
* [Technologies](#technologies)
* [Setup](#setup)
* [Status](#status)


## Screenshots
![Example screenshot - sign in](./img/signIn.png)
![Example screenshot - main page](./img/main.png)

## Technologies
* Java - jdk-15.0.1
* Spring modules (version 2.4.2): 
    * Spring Boot
    * Spring Security
    * Spring Data 
    * Spring Mail
* Hibernate - version 5.4.18.FINAL
* PostgreSQL - version 13.0
* ReactJS - version 16.13.1
* Docker - version 19.03.13
* TravisCI
* Google Cloud SQL
* Google Cloud Run

## Setup
Running system is very easy. After editing file *app\backend\src\main\resources\application.yml* or *travis.yml* (second file should be edited only if you want to use TravisCI) use commands `docker-compose build` and `docker-compose up`.

## Code Examples
Example of class, used in integrating system with social media accounts is presented below. Moreover worth mentioning is configurating transactions and retrying of transactions.

```java
@Log
@Service
@Retryable(
        maxAttempts = 5,
        backoff = @Backoff(delay = 10000),
        exclude = {AppBaseException.class}
)
@Transactional(
        isolation = Isolation.READ_COMMITTED,
        propagation = Propagation.REQUIRES_NEW,
        transactionManager = "mokTransactionManager",
        timeout = 5,
        rollbackFor = {OptimisticLockAppException.class}
)
public class OAuth2ServiceImpl implements OAuth2Service {
    private AccountRepository accountRepository;
    private AccessLevelRepository accessLevelRepository;

    @Autowired
    public OAuth2ServiceImpl(AccountRepository accountRepository, AccessLevelRepository accessLevelRepository) {
        this.accountRepository = accountRepository;
        this.accessLevelRepository = accessLevelRepository;
    }

    @Override
    public AccountEntity registerAccountOAuth2(String registrationId, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId, oAuth2User.getAttributes());
        if(StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException();
        }

        AccountEntity account = new AccountEntity(
                oAuth2UserInfo.getFirstName(),
                oAuth2UserInfo.getLastName(),
                oAuth2UserInfo.getEmail(),
                AuthProvider.valueOf(registrationId),
                oAuth2UserInfo.getId(),
                oAuth2UserInfo.getLanguage()
        );
        AccessLevelEntity clientRole = accessLevelRepository.findByRoleName(RoleName.ROLE_CLIENT)
                .orElseThrow(() -> new HttpBaseException("User Role not set."));

        account.setAccessLevelEntities(Collections.singleton(clientRole));

        BasketEntity basketEntity = new BasketEntity(account);
        account.setBasketEntity(basketEntity);

        return accountRepository.save(account);
    }

    @Override
    public AccountEntity updateAccountOAuth2(String registrationId, OAuth2User oAuth2User) throws AccountNotExistsException {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId, oAuth2User.getAttributes());
        if(StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException();
        }

        Optional<AccountEntity> accountOptional = accountRepository.findByEmail(oAuth2UserInfo.getEmail());
        AccountEntity existingAccount;
        if(!accountOptional.isPresent())
            throw new AccountNotExistsException();
        existingAccount = accountOptional.get();
        if (!existingAccount.getProvider().equals(AuthProvider.valueOf(registrationId))) {
            throw new OAuth2WrongProviderException();
        }
        existingAccount.setFirstName(oAuth2UserInfo.getFirstName());
        existingAccount.setLastName(oAuth2UserInfo.getLastName());

        return accountRepository.save(existingAccount);
    }
}
```


## Status
Project is:  _finished_.