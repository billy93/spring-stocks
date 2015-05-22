package net.cs50.finance.controllers;

import net.cs50.finance.models.Stock;
import net.cs50.finance.models.StockHolding;
import net.cs50.finance.models.StockLookupException;
import net.cs50.finance.models.User;
import net.cs50.finance.models.dao.StockHoldingDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Chris Bay on 5/17/15.
 */
@Controller
public class StockController extends AbstractFinanceController {

    @Autowired
    StockHoldingDao stockHoldingDao;

    @RequestMapping(value = "/quote", method = RequestMethod.GET)
    public String quoteForm(Model model) {

        // pass data to template
        model.addAttribute("title", "Quote");
        model.addAttribute("quoteNavClass", "active");
        return "quote_form";
    }

    @RequestMapping(value = "/quote", method = RequestMethod.POST)
    public String quote(String symbol, Model model) {

        // TODO - Implement quote lookup
        Stock stock = null;
        try {
            stock = Stock.lookupStock(symbol);
        } catch (StockLookupException e) {
            this.displayError("Unable to look up stock", model);
            e.printStackTrace();
        }

        // pass data to template
        model.addAttribute("stock_desc", stock.getSymbol());
        model.addAttribute("stock_price", stock.getPrice());
        model.addAttribute("title", "Quote");
        model.addAttribute("quoteNavClass", "active");

        return "quote_display";
    }

    @RequestMapping(value = "/buy", method = RequestMethod.GET)
    public String buyForm(Model model) {

        model.addAttribute("title", "Buy");
        model.addAttribute("action", "/buy");
        model.addAttribute("buyNavClass", "active");
        return "transaction_form";
    }

    @RequestMapping(value = "/buy", method = RequestMethod.POST)
    public String buy(String symbol, int numberOfShares, HttpServletRequest request, Model model) {

        // TODO - Implement buy action

        // get user
        User user = this.getUserFromSession(request);

        // conduct buy
        try {
            StockHolding.buyShares(user, symbol, numberOfShares);
        } catch (StockLookupException e) {
            this.displayError("Unable to buy", model);
            e.printStackTrace();
        }

        model.addAttribute("confirmMessage", "You bought some stock");
        model.addAttribute("title", "Buy");
        model.addAttribute("action", "/buy");
        model.addAttribute("buyNavClass", "active");

        return "transaction_confirm";
    }

    @RequestMapping(value = "/sell", method = RequestMethod.GET)
    public String sellForm(Model model) {
        model.addAttribute("title", "Sell");
        model.addAttribute("action", "/sell");
        model.addAttribute("sellNavClass", "active");
        return "transaction_form";
    }

    @RequestMapping(value = "/sell", method = RequestMethod.POST)
    public String sell(String symbol, int numberOfShares, HttpServletRequest request, Model model) {

        // TODO - Implement sell action

        model.addAttribute("title", "Sell");
        model.addAttribute("action", "/sell");
        model.addAttribute("sellNavClass", "active");

        return "transaction_confirm";
    }

}
