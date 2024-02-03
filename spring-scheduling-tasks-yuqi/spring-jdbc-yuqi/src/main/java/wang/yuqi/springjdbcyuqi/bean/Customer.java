package wang.yuqi.springjdbcyuqi.bean;

public class Customer {
    private Long id;

    private String lastname,firstname;

    private static volatile Customer instance;
    private static Customer getInstance(){
        //  第一次A，B谁都会进来 所以判空
        if (instance == null) {
            synchronized (Customer.class){
                // 多线程时A进来 判空后创建实例，释放锁后此时B进来，不判空会再创建一次实例
                if (instance == null) {
                    return instance =  new Customer();
                }

            }
        }
        return instance;

    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", lastname='" + lastname + '\'' +
                ", firstname='" + firstname + '\'' +
                '}';
    }

    public Customer() {
    }

    public Customer(Long id, String lastname, String firstname) {
        this.id = id;
        this.lastname = lastname;
        this.firstname = firstname;
    }

    public Long getId() {
        return id;
    }

    // 建造者模式
    public Customer setId(Long id) {
        this.id = id;
        return this;
    }

    public String getLastname() {
        return lastname;
    }

    public Customer setLastname(String lastname) {
        this.lastname = lastname;
        return this;
    }

    public String getFirstname() {
        return firstname;
    }

    public Customer setFirstname(String firstname) {
        this.firstname = firstname;
        return this;
    }
}
