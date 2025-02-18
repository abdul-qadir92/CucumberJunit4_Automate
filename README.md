to run a test on local browser - set the value in the src/test/resources/config/config.properties to respective browser

to test on brwoserstack - set the value as remote in the config.properties files.

then run the below command from cli with approriate config from src/test/resources/browserstack

mvn test -P "Suite-bs" -Dcaps-type="parallel" 

mvn test -P "Suite-bs"
