<h1>🔐 Password Manager Application</h1>
<h2>Security Fundamentals and Development Module Group Project</h2>

<p>
This project is part of a group project for the Security Fundamentals and Development module. It consists of a desktop password manager application built using <strong>JavaFX</strong> and focused on demonstrating
fundamental principles of <strong>application security</strong> and <strong>defensive programming</strong>.
</p>

<h3>🌟 Features</h3>
<ul>
  <li><strong>User Registration and Authentication:</strong> Secure signup and login with password strength checks.</li>
  <li><strong>Secure Password Storage:</strong> User passwords are not stored in plain text; they are hashed using BCrypt.</li>
  <li><strong>Password Strength Meter and Generator:</strong> Assists users in creating strong, complex passwords that meet specific requirements.</li>
  <li><strong>Input Validation and Sanitization:</strong> Implements measures to prevent common web security vulnerabilities.</li>
  <li><strong>Theming:</strong> Supports light and dark modes.</li>
  <li><strong>Database Flexibility:</strong> Can connect to a live PostgreSQL database (e.g., Supabase) or use an in-memory data store for local testing.</li>
</ul>

<h3>🛡️ Security Implementation Details</h3>
<p>This application incorporates several security best practices:</p>

<h4>Password Hashing (BCrypt)</h4>
<ul>
  <li>The <code>PasswordHash</code> class uses the BCrypt algorithm with a cost factor of 12 to generate secure, one-way hashes of user passwords, including a random salt for each user.</li>
  <li><code>PasswordHash.hash(plainPassword)</code> is used during registration (<code>RegisterController.onClickSignUp</code>).</li>
</ul>

<h4>Authentication (Constant-Time Comparison)</h4>
<ul>
  <li>The <code>AuthManager</code> class handles user login.</li>
  <li>It uses <code>BCrypt.checkpw(plainPassword, storedHash)</code> for password verification, which is resistant to timing attacks.</li>
  <li>To further prevent timing attacks when a user does not exist, a fake BCrypt hash is checked against the input password (<code>AuthManager.Fake_BCRYPT_HASH</code>).</li>
</ul>

<h4>Input Validation and SQL Injection Prevention</h4>
<ul>
  <li>The <code>SQLInjectionPrevention</code> utility class provides methods for:
    <ul>
      <li>Email validation (<code>validateEmail</code>) using a regular expression and length check.</li>
      <li>Text cleaning (<code>cleanText</code>) to remove control characters.</li>
      <li>SQL LIKE escaping (<code>sanitizeLike</code>) to handle wildcards in search terms.</li>
      <li>Safe column ordering (<code>getSafeOrderBy</code>) via a whitelist of allowed column names.</li>
    </ul>
  </li>
  <li>Database interaction in PostgreSQL uses <strong>PreparedStatement</strong> with validated and sanitized inputs as the primary defense against SQL Injection.</li>
</ul>

<h4>Password Requirements</h4>
<ul>
  <li>Registration enforces a minimum length of 12 characters and the inclusion of uppercase, lowercase, numbers, and symbols (<code>RegisterController.arePasswordRequirementsMet</code>).</li>
  <li>The Password Strength Meter uses an Entropy calculation to provide a score, guiding users to strong passwords.</li>
</ul>

<h4>Login Rate Limiting</h4>
<ul>
  <li>The <code>LogInController</code> implements a mechanism to track failed login attempts.</li>
  <li>After 5 failed attempts, the login button is disabled for 30 seconds to mitigate brute-force attacks (<code>handleLoginAttempts</code>).</li>
</ul>

<h3>🛠️ Prerequisites</h3>
<ul>
  <li>Java 17+</li>
  <li>Maven (for dependency management)</li>
</ul>

<h3>🚀 Getting Started</h3>
<p><strong>Clone the repository:</strong></p>
<pre><code>git clone https://github.com/stefanyrjunges/cryptography-software-solution
cd password-manager-app
</code></pre>

<p><strong>Set up Database (Optional):</strong></p>
<p>The application will automatically use the InMemoryDatabase if no environment variables are set.</p>
<p>To use a PostgreSQL database, set the following environment variables:</p>
<pre><code>export APP_JDBC_URL="jdbc:postgresql://&lt;host&gt;:&lt;port&gt;/&lt;database&gt;"
export APP_DB_USER="&lt;username&gt;"
export APP_DB_PASSWORD="&lt;password&gt;"
</code></pre>

<p><strong>Run the application:</strong></p>
<pre><code># From the project root with Maven
mvn clean compile exec:java -Dexec.mainClass="org.example.passwordmanager.Start"
</code></pre>

Alternatively, run the Start.java class directly from your IDE (e.g., IntelliJ).
Alternatively, run the Start.java class directly from your IDE (e.g., IntelliJ).
<p>Alternatively, run the <code>Start.java</code> class directly from your IDE (e.g., IntelliJ).</p>
