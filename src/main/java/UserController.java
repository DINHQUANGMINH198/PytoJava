@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<User> getUser(@RequestHeader(value="api-key") String apiKey,
                                        @RequestParam(value="email") String email,
                                        @RequestParam(value="phone") String phone) {
        if (!isApiKeyValid(apiKey)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        User user = userRepository.findByEmailAndPhone(email, phone);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    private boolean isApiKeyValid(String apiKey) {
        // Implement validation logic here
        return true;
    }
}
