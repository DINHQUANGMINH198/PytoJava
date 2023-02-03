public interface UserRepository {
    User findByEmailAndPhone(String email, String phone);
}
