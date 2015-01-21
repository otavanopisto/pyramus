DELETE FROM Defaults;

DELETE FROM MagicKey;

DELETE FROM PluginRepository;
  
DELETE FROM hibernate_sequences where sequence_name = 'Defaults';

DELETE FROM hibernate_sequences where sequence_name = 'MagicKey';

DELETE FROM hibernate_sequences where sequence_name = 'PluginRepository';