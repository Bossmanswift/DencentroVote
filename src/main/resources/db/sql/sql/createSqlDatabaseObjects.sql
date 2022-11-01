IF  NOT EXISTS (SELECT * FROM sys.databases WHERE name = N'decentrovote')
BEGIN
        CREATE DATABASE [decentrovote]
END;
GO

USE [decentrovote]
GO

DROP TABLE IF EXISTS dbo.[Choice]
DROP TABLE IF EXISTS dbo.[VoterRecord]

CREATE TABLE
    dbo.Choice
(
    [choiceId] int IDENTITY(1,1) PRIMARY KEY NOT NULL,
    [choiceName] [VARCHAR] (50) NOT NULL,
    [choiceDescription] [VARCHAR] (255) NOT NULL,
    [electionId] int NULL,
    [createdDate] [DATETIME] NULL
)
    GO

CREATE TABLE
    dbo.VoteRecord
(
    [voteRecordId] VARCHAR (255) PRIMARY KEY NOT NULL,
    [choiceName] [VARCHAR] (50) NOT NULL,
    [choiceDescription] [VARCHAR] (255) NOT NULL,
    [electionDescription] [VARCHAR] (255) NOT NULL,
    [createdDate] [DATETIME] NULL
)
    GO

CREATE TABLE
    dbo.Role
(
    [roleId] bigint IDENTITY(1,1) PRIMARY KEY NOT NULL,
    [name] [VARCHAR] (50) NOT NULL,
    [description] [VARCHAR] (255) NOT NULL,
)
    GO


CREATE TABLE
    dbo.[User]
(
    [userId] BIGINT IDENTITY(1,1) PRIMARY KEY NOT NULL,
    [username] [VARCHAR] (50) NOT NULL,
    [name] [VARCHAR] (255) NOT NULL,
    [password] [VARCHAR] (255) NOT NULL,
    [encryptedMnemonicCode] [VARCHAR] (255) NOT NULL
    )
    GO

CREATE TABLE
    dbo.UserRoles
(
    [userId] BIGINT NOT NULL,
    [roleId] BIGINT NOT NULL,
     PRIMARY KEY (userId, roleId)
)
    GO

INSERT INTO dbo.[Role] (name, description) VALUES ('SYS_ADMIN', 'System Administrator Role');
INSERT INTO dbo.[Role] (name, description) VALUES ('ELECT_ADMIN','Election Administrator Role');
INSERT INTO dbo.[Role] (name, description) VALUES ('VOTER','Voter Role');