
@Repository
public class UserRepositoryImpl implements UserRepository {

    private final DSLContext dsl;

    public UserRepositoryImpl(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public User findByEmailAndPhone(String email, String phone) {
        return dsl.selectFrom(USER)
                .where(USER.EMAIL.eq(email))
                .and(USER.PHONE.eq(phone))
                .fetchOneInto(User.class);
    }
}

