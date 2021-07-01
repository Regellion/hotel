package service;

import dto.UserDto;
import model.User;
import repository.UserRepository;
import repository.UserRepositoryImpl;

import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl() {
        userRepository = new UserRepositoryImpl();
    }

    @Override
    public User createUser(UserDto userDto) {
        return Optional.ofNullable(userRepository.saveUser(new User(userDto.getName())))
                .orElseThrow(() -> new RuntimeException("Failed user creation"));
    }

    @Override
    public List<User> getAllUsers() {
        List<User> userList = userRepository.getAllUsers();
        if (userList.size() == 0) {
            throw new RuntimeException("Users list is empty");
        }
        return userList;
    }

    @Override
    public User getUserById(Long id) {
        return Optional.ofNullable(userRepository.getUserById(id)).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public void updateUserById(Long id, UserDto userDto) {
        User tempUser = Optional.ofNullable(userRepository.getUserById(id)).orElseThrow(() -> new RuntimeException("User not found"));
        tempUser.setName(userDto.getName());
        userRepository.updateUserById(tempUser);
    }

    @Override
    public void deleteAllUsers() {
        List<User> userList = userRepository.getAllUsers();
        if (userList.size() == 0) {
            throw new RuntimeException("Users list is empty");
        }
        userRepository.deleteAllUsers();
    }

    @Override
    public void deleteUserById(Long id) {
        User tempUser = Optional.ofNullable(userRepository.getUserById(id)).orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.deleteUserById(tempUser.getId());
    }
}
