AnnotationDao
=============

#English Version
This project implements a simple object relationshiop mapping using java annotion.

###Samples:
<pre><code>
//Entity class
@Table(name = "dog")
public class Dog {
	private boolean alive;
	private int id;
	private String name;

	public Dog() {

	}

	public Dog(boolean alive, String name) {
		this.alive = alive;
		this.name = name;
	}

	@Id(name = "id")
	@Column(name = "id", type = DataType.Integer)
	public int getId() {
		return id;
	}

	@Column(name = "name", type = DataType.Varchar, length = 20)
	public String getName() {
		return name;
	}

	@Column(name = "alive", type = DataType.Boolean)
	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}
}

//insert
AnnotationDao<Dog> dao = new AnnotationDao<Dog>(this, Dog.class);

String name = "dog1";
boolean alive = true;

Dog dog = new Dog(alive, name);
dao.insert(dog);

//query
//list all
List<Dog> list = dao.list();
//query by id
dao.query(3);

//delete
dao.delete(dog);

//update
dog.setName("dog2");
dao.update(dog);
</code></pre>

#中文版
本项目利用Java注解实现了简单的Android SQLite O/R Mapping库。

###示例：
<pre><code>
//实体类
@Table(name = "dog")
public class Dog {
	private boolean alive;
	private int id;
	private String name;

	public Dog() {

	}

	public Dog(boolean alive, String name) {
		this.alive = alive;
		this.name = name;
	}

	@Id(name = "id")
	@Column(name = "id", type = DataType.Integer)
	public int getId() {
		return id;
	}

	@Column(name = "name", type = DataType.Varchar, length = 20)
	public String getName() {
		return name;
	}

	@Column(name = "alive", type = DataType.Boolean)
	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}
}

//添加
AnnotationDao<Dog> dao = new AnnotationDao<Dog>(this, Dog.class);

String name = "dog1";
boolean alive = true;

Dog dog = new Dog(alive, name);
dao.insert(dog);

//查找
//列出所有对象
List<Dog> list = dao.list();
//根据ID查询
dao.query(3);

//删除
dao.delete(dog);

//更新
dog.setName("dog2");
dao.update(dog);
</code></pre>