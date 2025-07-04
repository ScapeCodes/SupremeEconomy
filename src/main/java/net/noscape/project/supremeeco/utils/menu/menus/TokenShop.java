package net.noscape.project.supremeeco.utils.menu.menus;

import net.noscape.project.supremeeco.*;
import net.noscape.project.supremeeco.managers.*;
import net.noscape.project.supremeeco.utils.*;
import net.noscape.project.supremeeco.utils.menu.*;
import org.bukkit.*;
import org.bukkit.enchantments.*;
import org.bukkit.entity.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;

import java.util.*;
import java.util.stream.*;

public class TokenShop extends Menu {

    private final SupremeEconomy te = SupremeEconomy.getPlugin(SupremeEconomy.class);

    public TokenShop(MenuUtil menuUtil) {
        super(menuUtil);
    }

    @Override
    public String getMenuName() {
        return Utils.applyFormat(SupremeEconomy.getConfigManager().getTitleShop());
    }

    @Override
    public int getSlots() {
        return SupremeEconomy.getConfigManager().getSlotsShop();
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();

        for (String items : Objects.requireNonNull(SupremeEconomy.getConfigManager().getTokenExchange().getConfigurationSection("gui.items")).getKeys(false)) {
            String displayname = SupremeEconomy.getConfigManager().getTokenExchange().getString("gui.items." + items + ".displayname");
            int tokens = SupremeEconomy.getConfigManager().getTokenExchange().getInt("gui.items." + items + ".tokens");
            int amount_material = SupremeEconomy.getConfigManager().getTokenExchange().getInt("gui.items." + items + ".amount");


            if (Objects.requireNonNull(Objects.requireNonNull(e.getCurrentItem()).getItemMeta()).getDisplayName().equals(Utils.applyFormat(displayname))) {
                if (hasMaterial(player.getInventory(), amount_material)) {
                    e.setCancelled(true);
                    EconomyManager ptokens = SupremeEconomy.getTokenManager(player);
                    if (!(ptokens.getTokens() >= SupremeEconomy.getConfigManager().getConfig().getInt("t.player.max-balance"))) {
                        // remove material from inventory
                        removeMaterial(player.getInventory(), amount_material);

                        // get and add tokens
                        if (!SupremeEconomy.getConfigManager().isBankBalanceShop()) {
                            EconomyManager token = SupremeEconomy.getTokenManager(player);
                            token.addTokens(tokens);
                        } else {
                            BankManager bank = SupremeEconomy.getBankManager(player);
                            bank.addBank(tokens);
                        }

                        // confirmation
                        player.sendMessage(Utils.applyFormat(Objects.requireNonNull(SupremeEconomy.getConfigManager().getMessages().getString("m.RECEIVED")).replaceAll("%tokens%", String.valueOf(tokens)).replaceAll("%PREFIX%", SupremeEconomy.getConfigManager().getPrefix())));
                        if (SupremeEconomy.getConfigManager().getTokenExchange().getBoolean("gui.sound.enable")) {
                            player.playSound(player.getLocation(),
                                    Sound.valueOf(Objects.requireNonNull(SupremeEconomy.getConfigManager().getTokenExchange().getString("gui.sound.success")).toUpperCase()), 1, 1);
                        }
                    } else {
                        player.sendMessage(Utils.applyFormat("&cYou have reached the max token balance!"));
                    }
                } else {
                    player.sendMessage(Utils.applyFormat(Objects.requireNonNull(SupremeEconomy.getConfigManager().getMessages().getString("m.NOT_ENOUGH_MATERIALS")).replaceAll("%PREFIX%", SupremeEconomy.getConfigManager().getPrefix())));
                    if (SupremeEconomy.getConfigManager().getTokenExchange().getBoolean("gui.sound.enable")) {
                        player.playSound(player.getLocation(),
                                Sound.valueOf(Objects.requireNonNull(SupremeEconomy.getConfigManager().getTokenExchange().getString("gui.sound.failed")).toUpperCase()), 1, 1);
                    }
                }
            }
        }
    }

    @Override
    public void setMenuItems() {


        // main items

        for (String items : Objects.requireNonNull(SupremeEconomy.getConfigManager().getTokenExchange().getConfigurationSection("gui.items")).getKeys(false)) {

            String displayname = SupremeEconomy.getConfigManager().getTokenExchange().getString("gui.items." + items + ".displayname");
            int slot = SupremeEconomy.getConfigManager().getTokenExchange().getInt("gui.items." + items + ".slot");
            boolean glow = SupremeEconomy.getConfigManager().getTokenExchange().getBoolean("gui.items." + items + ".glow");
            String material = Objects.requireNonNull(SupremeEconomy.getConfigManager().getTokenExchange().getString("gui.items." + items + ".material")).toUpperCase();
            int amount = SupremeEconomy.getConfigManager().getTokenExchange().getInt("gui.items." + items + ".amount");

            //List<String> lore = TokensEconomy.getConfigManager().getTokenshop().getStringList("gui.items." + items + ".lore");

            ItemStack item = new ItemStack(Material.valueOf(material), amount);

            for (String lore : SupremeEconomy.getConfigManager().getTokenExchange().getStringList("gui.items." + items + ".lore")) {
                nameItem(item, Utils.applyFormat(displayname), lore);
            }

            if (glow) {
                item.addUnsafeEnchantment(Enchantment.FLAME, 1);
            }

            inventory.setItem(slot, item);
        }

        // fill glass
        ItemStack glass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
        ItemMeta glass_meta = glass.getItemMeta();
        assert glass_meta != null;
        glass_meta.setDisplayName(Utils.applyFormat("&8*"));
        glass.setItemMeta(glass_meta);

        // loop through empty slots and set glass
        for(int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, glass);
            }
        }
    }

    private void removeMaterial(Inventory inventory, int amount) {
        if (amount <= 0) return;
        int size = inventory.getSize();
        for (int slot = 0; slot < size; slot++) {
            ItemStack is = inventory.getItem(slot);
            if (is == null) continue;
            if (Material.valueOf(Objects.requireNonNull(SupremeEconomy.getConfigManager().getTokenExchange().getString("gui.item-exchange")).toUpperCase()) == is.getType()) {
                int newAmount = is.getAmount() - amount;
                if (newAmount > 0) {
                    is.setAmount(newAmount);
                    break;
                } else {
                    inventory.clear(slot);
                    amount = -newAmount;
                    if (amount == 0) break;
                }
            }
        }
    }

    private boolean hasMaterial(Inventory inventory, int amount) {
        int size = inventory.getSize();
        for (int slot = 0; slot < size; slot++) {
            ItemStack is = inventory.getItem(slot);
            if (is == null) continue;
            if (Material.valueOf(Objects.requireNonNull(SupremeEconomy.getConfigManager().getTokenExchange().getString("gui.item-exchange")).toUpperCase()) == is.getType()
                    && is.getAmount() >= amount)
                return true;
        }

        return false;
    }

    private ItemStack nameItem(final ItemStack item, final String name, final String... lore) { //  Changes String lore to String... Lore
        final ItemMeta item_meta = item.getItemMeta();
        assert item_meta != null;
        item_meta.setDisplayName(name); // You should most likely translate here too unless you do it beforehand.
        /* Setting the lore but translating the color codes as well. */
        item_meta.setLore(Arrays.stream(lore).map(line -> ChatColor.translateAlternateColorCodes('&', line)).collect(Collectors.toList()));

        item_meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item_meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        item_meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item_meta.addItemFlags(ItemFlag.HIDE_DESTROYS);

        item.setItemMeta(item_meta);
        return item;
    }
}
