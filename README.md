# LobotomyInc


Lobotomise and optimise your villager by renaming them. You can configure names in mod config


<img width="400" height="400" alt="icon" src="https://github.com/user-attachments/assets/514e747e-6137-4c76-a3fd-d085b249653e" />


## Default config
```
{
  "lobotomize_villagers_enabled": true,
  "free_nametag": false,
  "exact_names": [
    "lobotomized"
  ],
  "advanced_matching": {
    "match_contains": false,
    "contains_list": [
      "@"
    ],
    "match_prefix": false,
    "prefix_list": [
      "!"
    ],
    "match_suffix": false,
    "suffix_list": [
      "#"
    ],
    "match_surround": false,
    "surround_list": [
      "$"
    ]
  }
}
```

- "free_nametag" - Naming villager with name(s) in config does not consume the nametag
- "match_contains" - Villager name contains specific symbol
- "match_prefix" - Specific symbol is prefix(start) of villager name
- "match_suffix" - Specific symbol is suffix(end) of villager name
- "match_surround" - Villager name is surrounded by specific symbol

Write names like this ["name1", "name2" ...]


## How it works
Optimised villager have their pathfinding fully disabled so it's best to use mod for optimizing tradehalls while having villager in 1x1 box near job block. Their brain is turned off until trades need to be restock.
