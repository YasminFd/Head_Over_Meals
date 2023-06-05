:: Create database and import the data

set /p username=Enter MySQL username:
::set /p password=Enter MySQL password:

echo Creating database and importing the data...
mysql -u %username% -p  -e "CREATE DATABASE IF NOT EXISTS homdb;"
mysql -u %username% -p homdb < homdb.sql
pause