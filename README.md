Using Docker:

docker build -t foosbot-service
docker run -it foosbot-service


To create psql database:

CREATE TABLE results(
    match_uuid        char(36) PRIMARY KEY,
    reporter          varchar(50),         
    team1p1           varchar(50),    
    team1p2           varchar(50),    
    team2p1           varchar(50),
    team2p2           varchar(50),    
    team1score        int,          
    team2score        int,         
    timestamp         timestamp
);