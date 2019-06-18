package me.abhi.survival.listener;

import lol.polar.core.Core;
import me.abhi.survival.Survival;
import me.abhi.survival.claim.Claim;
import me.abhi.survival.data.PlayerData;
import me.abhi.survival.team.Team;
import me.abhi.survival.util.Cuboid;
import me.abhi.survival.util.Items;
import me.abhi.survival.util.ScoreHelper;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

public class PlayerListener implements Listener {

    private Survival plugin;

    public PlayerListener(Survival plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        this.plugin.getManagerHandler().getPlayerDataManager().addPlayer(player);
        ScoreHelper.createScore(player);
        this.plugin.getManagerHandler().getPlayerManager().findTeam(player);
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(100D);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(player);
        playerData.save();
        this.plugin.getManagerHandler().getPlayerDataManager().removePlayer(player);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(player);
        for (Claim claim : this.plugin.getManagerHandler().getClaimManager().getClaimList()) {
            if (claim.getCuboid().contains(player.getLocation()) && (playerData.getClass() == null || playerData.getInClaim() != claim)) {
                playerData.setInClaim(claim);
                player.sendMessage(ChatColor.YELLOW + "Now entering: " + ChatColor.GREEN + this.plugin.getServer().getOfflinePlayer(claim.getOwner()).getName() + "'s Claim");
            }
        }
        if (playerData.getInClaim() != null) {
            if (!playerData.getInClaim().getCuboid().contains(player.getLocation())) {
                player.sendMessage(ChatColor.YELLOW + "Now leaving: " + ChatColor.GREEN + this.plugin.getServer().getOfflinePlayer(playerData.getInClaim().getOwner()).getName() + "'s Claim");
                playerData.setInClaim(null);
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        PlayerData playerData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(player);
        playerData.setDeaths(playerData.getDeaths() + 1);
        if (player.getKiller() instanceof Player) {
            Player killer = player.getKiller();
            PlayerData killerData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(killer);
            killerData.setKills(killerData.getKills() + 1);
        }
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player damager = (Player) event.getDamager();
            Entity entity = event.getEntity();
            if (this.plugin.getManagerHandler().getClaimManager().isClaimed(entity.getLocation())) {
                Claim claim = this.plugin.getManagerHandler().getClaimManager().getClaim(entity.getLocation());
                if (!claim.getOwner().equals(damager.getUniqueId()) && !claim.getPermitees().contains(damager.getUniqueId())) {
                    event.setCancelled(true);
                    damager.sendMessage(ChatColor.RED + "You cannot attack in this claim!");
                }
            }
        }
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player player = (Player) event.getEntity();
            Player damager = (Player) event.getDamager();
            PlayerData damagerData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(damager);
            if (damagerData.hasTeam()) {
                Team team = damagerData.getTeam();
                if (team.getMembers().contains(player.getUniqueId().toString())) {
                    event.setCancelled(true);
                    damager.sendMessage(ChatColor.RED + "You cannot hurt players that are on your team!");
                }
            }
        } else if (event.getEntity() instanceof Player && event.getDamager() instanceof Projectile) {
            Player player = (Player) event.getEntity();
            Projectile projectile = (Projectile) event.getDamager();
            if (projectile.getShooter() instanceof Player) {
                Player damager = (Player) projectile.getShooter();
                PlayerData damagerData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(damager);
                if (damagerData.hasTeam()) {
                    Team team = damagerData.getTeam();
                    if (team.getMembers().contains(player.getUniqueId().toString())) {
                        event.setCancelled(true);
                        damager.sendMessage(ChatColor.RED + "You cannot hurt players that are on your team!");
                    }
                }
            }
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(player);
        lol.polar.core.data.PlayerData coreData = Core.getInstance().getManagerHandler().getPlayerDataManager().getPlayerData(player);
        String format = ChatColor.GREEN + player.getName() + ChatColor.DARK_GRAY + ": " + ChatColor.RESET + event.getMessage().replace("%", "%%");
        if (coreData.getGroup() != null) {
            try {
                format = ChatColor.translateAlternateColorCodes('&', coreData.getGroup().getPrefix()) + " " + player.getName() + ChatColor.DARK_GRAY + ": " + ChatColor.RESET + event.getMessage().replace("%", "%%");
            } catch (Exception ex) {
            }
            if (playerData.hasTeam()) {
                format = ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + playerData.getTeam().getName() + ChatColor.DARK_GRAY + "] " + format;
            }
        }
        event.setFormat(format);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(player);
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (player.getItemInHand().equals(Items.CLAIM_WAND.getItem())) {
                event.setCancelled(true);
                if (playerData.getLocation1() != null && playerData.getLocation1().equals(event.getClickedBlock().getLocation())) {
                    return;
                }
                if (this.plugin.getManagerHandler().getClaimManager().isClaimed(event.getClickedBlock().getLocation())) {
                    player.sendMessage(ChatColor.RED + "You cannot claim over someone else's territory!");
                } else if (!this.plugin.getManagerHandler().getClaimManager().claimiableDistance(event.getClickedBlock().getLocation(), 1)) {
                    player.sendMessage(ChatColor.RED + "Your claim must be at least 1 block away from someone another claim!");
                } else {
                    playerData.setLocation1(event.getClickedBlock().getLocation());
                    player.sendMessage(ChatColor.YELLOW + "You have set the first point of your claim!");
                    if (playerData.getLocation2() != null) {
                        int price = (int) Math.round(new Location(playerData.getLocation1().getWorld(), playerData.getLocation1().getX(), 0, playerData.getLocation1().getZ()).distance(new Location(playerData.getLocation2().getWorld(), playerData.getLocation2().getX(), 0, playerData.getLocation2().getZ())) * 7);
                        if (price < 40) {
                            player.sendMessage(ChatColor.RED + "Your claim must be a minimum of 5x5!");
                        } else {
                            player.sendMessage(ChatColor.YELLOW + "This claim will cost: " + (playerData.getBalance() < price ? ChatColor.RED + "$" + price : ChatColor.GREEN + "$" + price));
                        }
                    }
                }
            }
        }
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getClickedBlock().getState() instanceof Sign) {
                Sign sign = (Sign) event.getClickedBlock().getState();
                if (sign.getLine(0).equalsIgnoreCase(ChatColor.DARK_BLUE + "[Buy]")) {
                    String[] data = sign.getLine(1).split(":");
                    int amount = Integer.parseInt(sign.getLine(2));
                    int cost = Integer.parseInt(sign.getLine(3).replace("$", ""));
                    if (playerData.getBalance() < cost) {
                        player.sendMessage(ChatColor.RED + "You have insufficient funds!");
                    } else {
                        try {
                            ItemStack itemStack = new ItemStack(Material.getMaterial(Integer.parseInt(data[0])), amount, (short) (data.length > 1 ? Integer.parseInt(data[1]) : 0));
                            playerData.setBalance(playerData.getBalance() - cost);
                            player.getInventory().addItem(itemStack);
                            player.updateInventory();
                            player.sendMessage(ChatColor.GREEN + "You have bought " + amount + " " + (Material.getMaterial(Integer.parseInt(data[0])).toString() + "'s for $" + cost + "!"));
                        } catch (Exception ex) {
                            player.sendMessage(ChatColor.RED + "There was an error processing this transaction!");
                        }
                    }
                } else if (sign.getLine(0).equalsIgnoreCase(ChatColor.DARK_BLUE + "[Sell]")) {
                    String[] data = sign.getLine(1).split(":");
                    int amount = Integer.parseInt(sign.getLine(2));
                    int cost = Integer.parseInt(sign.getLine(3).replace("$", ""));
                    try {
                        boolean hasItem = false;
                        ItemStack itemStack = new ItemStack(Material.getMaterial(Integer.parseInt(data[0])), amount, (short) (data.length > 1 ? Integer.parseInt(data[1]) : 0));
                        for (int i = 0; i <= 35; i++) {
                            if (player.getInventory().getItem(i) != null && player.getInventory().getItem(i).isSimilar(itemStack)) {
                                ItemStack item = player.getInventory().getItem(i);
                                item.setAmount(item.getAmount() - amount);
                                player.getInventory().setItem(i, item);
                                playerData.setBalance(playerData.getBalance() + cost);
                                player.updateInventory();
                                player.sendMessage(ChatColor.GREEN + "You have sold " + amount + " " + (Material.getMaterial(Integer.parseInt(data[0])).toString() + "'s for $" + cost + "!"));
                                hasItem = true;
                                return;
                            }
                        }
                        if (!hasItem) {
                            player.sendMessage(ChatColor.RED + "You do not have the correct item!");
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        player.sendMessage(ChatColor.RED + "There was an error processing this transaction!");
                    }
                }
            }
            if (player.getItemInHand().equals(Items.CLAIM_WAND.getItem())) {
                event.setCancelled(true);
                if (playerData.getLocation2() != null && playerData.getLocation2().equals(event.getClickedBlock().getLocation())) {
                    return;
                }
                if (this.plugin.getManagerHandler().getClaimManager().isClaimed(event.getClickedBlock().getLocation())) {
                    player.sendMessage(ChatColor.RED + "You cannot claim over someone else's territory!");
                } else if (!this.plugin.getManagerHandler().getClaimManager().claimiableDistance(event.getClickedBlock().getLocation(), 1)) {
                    player.sendMessage(ChatColor.RED + "Your claim must be at least 1 block away from someone another claim!");
                } else {
                    playerData.setLocation2(event.getClickedBlock().getLocation());
                    player.sendMessage(ChatColor.YELLOW + "You have set the second point of your claim!");
                    if (playerData.getLocation1() != null) {
                        int price = (int) Math.round(new Location(playerData.getLocation1().getWorld(), playerData.getLocation1().getX(), 0, playerData.getLocation1().getZ()).distance(new Location(playerData.getLocation2().getWorld(), playerData.getLocation2().getX(), 0, playerData.getLocation2().getZ())) * 7);
                        if (price < 40) {
                            player.sendMessage(ChatColor.RED + "Your claim must be a minimum of 5x5!");
                        } else {
                            player.sendMessage(ChatColor.YELLOW + "This claim will cost: " + (playerData.getBalance() < price ? ChatColor.RED + "$" + price : ChatColor.GREEN + "$" + price));
                            return;
                        }
                    }
                }
            }
            if (this.plugin.getManagerHandler().getClaimManager().isClaimed(event.getClickedBlock().getLocation())) {
                Claim claim = this.plugin.getManagerHandler().getClaimManager().getClaim(event.getClickedBlock().getLocation());
                if (claim.getPermitees().contains(player.getUniqueId())) {
                    return;
                }
                if (claim.getOwner().equals(player.getUniqueId())) {
                    return;
                }
                if (player.hasPermission("survival.bypass")) {
                    return;
                }
                if (playerData.hasTeam()) {
                    Team team = playerData.getTeam();
                    if (!team.getMembers().contains(claim.getOwner().toString())) {
                        event.setCancelled(true);
                        player.sendMessage(ChatColor.RED + "You don't have " + this.plugin.getServer().getOfflinePlayer(claim.getOwner()).getName() + "'s permission!");
                    }
                } else {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You don't have " + this.plugin.getServer().getOfflinePlayer(claim.getOwner()).getName() + "'s permission!");
                }
            }
        }
        if (event.getAction() == Action.RIGHT_CLICK_AIR) {
            if (player.getItemInHand().equals(Items.CLAIM_WAND.getItem()) && player.isSneaking()) {
                event.setCancelled(true);
                player.getInventory().remove(Items.CLAIM_WAND.getItem());
                playerData.setLocation1(null);
                playerData.setLocation2(null);
                player.updateInventory();
                player.sendMessage(ChatColor.RED + "The claim was cleared!");
            }
        }
        if (event.getAction() == Action.LEFT_CLICK_AIR) {
            if (player.getItemInHand().equals(Items.CLAIM_WAND.getItem()) && player.isSneaking()) {
                event.setCancelled(true);
                if (playerData.getLocation1() == null || playerData.getLocation2() == null) {
                    player.sendMessage(ChatColor.RED + "You did not select both points!");
                } else {
                    int price = (int) Math.round(playerData.getLocation1().distance(playerData.getLocation2()) * 7);
                    if (playerData.getBalance() < price) {
                        player.sendMessage(ChatColor.RED + "You have insufficient funds!");
                    } else {
                        playerData.setBalance(playerData.getBalance() - price);
                        Cuboid cuboid = new Cuboid(new Location(playerData.getLocation1().getWorld(), playerData.getLocation1().getX(), 0, playerData.getLocation1().getZ()), new Location(playerData.getLocation2().getWorld(), playerData.getLocation2().getX(), 256, playerData.getLocation2().getZ()));
                        if (!this.plugin.getManagerHandler().getClaimManager().isClaimable(cuboid)) {
                            player.sendMessage(ChatColor.RED + "Another player's claim is within your selection. Please select another claim!");
                            return;
                        }
                        Claim claim = new Claim(player.getUniqueId(), cuboid, price);
                        this.plugin.getManagerHandler().getClaimManager().addClaim(claim);
                        player.getInventory().remove(Items.CLAIM_WAND.getItem());
                        playerData.setLocation1(null);
                        playerData.setLocation2(null);
                        player.updateInventory();
                        player.sendMessage(ChatColor.GREEN + "You have successfully claimed the land!");
                        player.sendMessage(ChatColor.GREEN + "Your balance has been updated to $" + playerData.getBalance() + "!");
                        return;
                    }
                }
            }
        }

    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(player);
        if (event.getItemDrop().getItemStack().equals(Items.CLAIM_WAND.getItem())) {
            event.setCancelled(true);
            player.getInventory().remove(Items.CLAIM_WAND.getItem());
            playerData.setLocation1(null);
            playerData.setLocation2(null);
            player.updateInventory();
            player.sendMessage(ChatColor.RED + "The claim was cleared!");
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(player);
        if (this.plugin.getManagerHandler().getClaimManager().isClaimed(event.getBlock().getLocation())) {
            Claim claim = this.plugin.getManagerHandler().getClaimManager().getClaim(event.getBlock().getLocation());
            if (claim.getPermitees().contains(player.getUniqueId())) {
                return;
            }
            if (claim.getOwner().equals(player.getUniqueId())) {
                return;
            }
            if (player.hasPermission("survival.bypass")) {
                return;
            }
            if (playerData.hasTeam()) {
                Team team = playerData.getTeam();
                if (!team.getMembers().contains(claim.getOwner().toString())) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You don't have " + this.plugin.getServer().getOfflinePlayer(claim.getOwner()).getName() + "'s permission!");
                }
            } else {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "You don't have " + this.plugin.getServer().getOfflinePlayer(claim.getOwner()).getName() + "'s permission!");
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(player);
        if (this.plugin.getManagerHandler().getClaimManager().isClaimed(event.getBlock().getLocation())) {
            Claim claim = this.plugin.getManagerHandler().getClaimManager().getClaim(event.getBlock().getLocation());
            if (claim.getPermitees().contains(player.getUniqueId())) {
                return;
            }
            if (claim.getOwner().equals(player.getUniqueId())) {
                return;
            }
            if (player.hasPermission("survival.bypass")) {
                return;
            }
            if (playerData.hasTeam()) {
                Team team = playerData.getTeam();
                if (!team.getMembers().contains(claim.getOwner().toString())) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You don't have " + this.plugin.getServer().getOfflinePlayer(claim.getOwner()).getName() + "'s permission!");
                }
            } else {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "You don't have " + this.plugin.getServer().getOfflinePlayer(claim.getOwner()).getName() + "'s permission!");
            }
        }
    }

    @EventHandler
    public void onSign(SignChangeEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("survival.admin") && event.getLine(0).equalsIgnoreCase("[BUY]")) {
            if (NumberUtils.isDigits(event.getLine(2)) && NumberUtils.isDigits(event.getLine(3))) {
                int cost = Integer.parseInt(event.getLine(3));
                int amount = Integer.parseInt(event.getLine(2));
                event.setLine(0, ChatColor.DARK_BLUE + "[Buy]");
                event.setLine(3, "$" + cost);
            }
        } else if (player.hasPermission("survival.admin") && event.getLine(0).equalsIgnoreCase("[SELL]")) {
            if (NumberUtils.isDigits(event.getLine(2)) && NumberUtils.isDigits(event.getLine(3))) {
                int cost = Integer.parseInt(event.getLine(3));
                int amount = Integer.parseInt(event.getLine(2));
                event.setLine(0, ChatColor.DARK_BLUE + "[Sell]");
                event.setLine(3, "$" + cost);
            }
        }
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent event) {
        event.setCancelled(true);
    }
}
