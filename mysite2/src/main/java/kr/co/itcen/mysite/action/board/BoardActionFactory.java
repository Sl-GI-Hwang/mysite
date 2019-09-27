package kr.co.itcen.mysite.action.board;

import kr.co.itcen.web.mvc.Action;
import kr.co.itcen.web.mvc.ActionFactory;

public class BoardActionFactory extends ActionFactory {

	@Override
	public Action getAction(String actionName) {
		// TODO Auto-generated method stub
		Action action = null;
		System.out.println(actionName);
		if("writeform".equals(actionName)) {
			action = new WriteformAction();
		} else if("write".equals(actionName)) {
			action = new WriteAction();
		} else if("readboard".equals(actionName)) {
			action = new ReadboardAction();
		} else if("modify".equals(actionName)) {
			action = new ModifyAction();
		} else if("modifyboard".equals(actionName)) {
			action = new ModifyboardAction();
		} else if("deleteform".equals(actionName)) {
			action = new DeleteboardAction();
		} else if("reply".equals(actionName)) {
			action = new ReplyAction();
		} else {
			//default (list)
			action = new ListAction();
		}
		return action;
	}
}
