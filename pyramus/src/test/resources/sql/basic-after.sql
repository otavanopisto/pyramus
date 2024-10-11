DELETE FROM Defaults;

DELETE FROM MagicKey;

DELETE FROM hibernate_sequences where sequence_name = 'Defaults';

DELETE FROM hibernate_sequences where sequence_name = 'MagicKey';
